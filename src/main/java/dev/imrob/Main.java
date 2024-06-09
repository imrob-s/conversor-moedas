package dev.imrob;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String API_KEY = System.getenv("API_KEY");
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    private static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Bem-vindo ao Conversor de Moedas!");
            System.out.println("Escolha uma conversão:");
            System.out.println("1. USD para BRL");
            System.out.println("2. EUR para USD");
            System.out.println("3. GBP para EUR");
            System.out.println("4. JPY para USD");
            System.out.println("5. AUD para CAD");
            System.out.println("6. CAD para BRL");
            System.out.println("7. Sair");
            System.out.print("Opção: ");
            int option = scanner.nextInt();
            if (option == 7) {
                break;
            }
            System.out.print("Digite o valor a ser convertido: ");
            double amount = scanner.nextDouble();
            convertCurrency(option, amount);
        }
        scanner.close();
    }

    private static void convertCurrency(int option, double amount) {
        String fromCurrency = "";
        String toCurrency = "";

        switch (option) {
            case 1:
                fromCurrency = "USD";
                toCurrency = "BRL";
                break;
            case 2:
                fromCurrency = "EUR";
                toCurrency = "USD";
                break;
            case 3:
                fromCurrency = "GBP";
                toCurrency = "EUR";
                break;
            case 4:
                fromCurrency = "JPY";
                toCurrency = "USD";
                break;
            case 5:
                fromCurrency = "AUD";
                toCurrency = "CAD";
                break;
            case 6:
                fromCurrency = "CAD";
                toCurrency = "BRL";
                break;
            default:
                System.out.println("Opção inválida.");
                return;
        }

        try {
            String jsonResponse = getApiResponse(fromCurrency);
            if (jsonResponse != null) {
                JSONObject json = new JSONObject(jsonResponse);
                double exchangeRate = json.getJSONObject("conversion_rates").getDouble(toCurrency);
                double convertedAmount = amount * exchangeRate;
                System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter taxa de câmbio: " + e.getMessage());
        }
    }

    private static String getApiResponse(String fromCurrency) throws IOException {
        String url = API_URL + fromCurrency;
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na resposta da API: " + response);
            }
            return response.body().string();
        }
    }
}
