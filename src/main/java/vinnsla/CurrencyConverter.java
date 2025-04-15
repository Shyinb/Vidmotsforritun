package vinnsla;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    // API key for exchangerate-api.com
    private static final String API_KEY = "8b27d792400b894cdbb6c399";
    // Base URL for the exchangerate-api.com API
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    // URL template for the current exchange rates
    private static final String LATEST_RATES_URL_TEMPLATE = BASE_URL + "%s/latest/%s";

    // Cache duration in hours
    private static final int CACHE_DURATION_HOURS = 24;

    private final CloseableHttpClient httpClient;

    // Cache for exchange rates
    private final Map<String, CachedRate> rateCache = new HashMap<>();

    public CurrencyConverter() {
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Converts an amount in ISK to the specified target currency.
     *
     * @param amount         Amount in ISK to convert.
     * @param targetCurrency Target currency code (e.g., "EUR", "USD").
     * @return The converted amount.
     * @throws IOException If an error occurs during the API call.
     */
    public double convert(int amount, String targetCurrency) throws IOException {
        if (amount == 0) {
            return 0;
        }

        // If converting to ISK, just return the amount
        if ("ISK".equalsIgnoreCase(targetCurrency)) {
            return amount;
        }

        // Get the exchange rate (how many ISK = 1 unit of target currency)
        double exchangeRate = getExchangeRate(targetCurrency);

        // Convert FROM ISK TO target currency by DIVIDING
        return amount / exchangeRate;
    }

    /**
     * Retrieves the current exchange rate between the specified currency and ISK.
     * Returns the rate in the format "1 [currency] = X ISK".
     *
     * @param currency The target currency code (e.g., "EUR", "USD").
     * @return The exchange rate as a value, how many ISK are needed for 1 unit of the target currency.
     * @throws IOException If an error occurs during the API call.
     */
    public double getExchangeRate(String currency) throws IOException {
        if ("ISK".equalsIgnoreCase(currency)) {
            return 1.0; // 1 ISK = 1 ISK
        }

        // Check if we have a valid cached rate
        if (rateCache.containsKey(currency)) {
            CachedRate cachedRate = rateCache.get(currency);
            if (cachedRate.isValid()) {
                return cachedRate.getRate();
            }
        }

        // If no valid cache, fetch from API
        double rate = fetchExchangeRateFromAPI(currency);

        // Cache the new rate
        rateCache.put(currency, new CachedRate(rate));

        return rate;
    }

    /**
     * Fetches the exchange rate from the API.
     *
     * @param currency The target currency code.
     * @return The exchange rate.
     * @throws IOException If an error occurs during the API call.
     */
    private double fetchExchangeRateFromAPI(String currency) throws IOException {
        String url = String.format(LATEST_RATES_URL_TEMPLATE, API_KEY, currency);
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String jsonResponse = EntityUtils.toString(entity);
            JSONObject json = new JSONObject(jsonResponse);

            // Check the result code
            String resultCode = json.getString("result");
            if (!"success".equals(resultCode)) {
                throw new IOException("API error: " + resultCode);
            }

            // The API returns exchange rates in the "conversion_rates" object
            JSONObject conversionRates = json.getJSONObject("conversion_rates");

            // Get the exchange rate for ISK
            if (!conversionRates.has("ISK")) {
                throw new IOException("Currency ISK not found in response");
            }

            // Here we get the value: 1 [currency] = X ISK
            return conversionRates.getDouble("ISK");
        }
    }

    /**
     * Clears the rate cache, forcing new API calls on the next request.
     */
    public void clearCache() {
        rateCache.clear();
    }

    /**
     * Closes the HTTP client used by this converter.
     *
     * @throws IOException if closing the client fails.
     */
    public void close() throws IOException {
        httpClient.close();
    }

    /**
     * Inner class to hold a cached exchange rate with its timestamp.
     */
    private class CachedRate {
        private final double rate;
        private final LocalDateTime timestamp;

        public CachedRate(double rate) {
            this.rate = rate;
            this.timestamp = LocalDateTime.now();
        }

        public double getRate() {
            return rate;
        }

        /**
         * Checks if the cached rate is still valid based on the cache duration.
         *
         * @return true if the rate is still valid, false otherwise.
         */
        public boolean isValid() {
            LocalDateTime now = LocalDateTime.now();
            long hoursDifference = ChronoUnit.HOURS.between(timestamp, now);
            return hoursDifference < CACHE_DURATION_HOURS;
        }
    }
}