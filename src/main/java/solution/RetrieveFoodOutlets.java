package solution;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import solution.model.FoodOutletsData;
import solution.model.FoodOutletsResponse;

/**
 * The objective is to retrieve and process this data to identify food outlets in a specified city that have an estimated cost below a given threshold
 */
public class RetrieveFoodOutlets {
    private final String url;
    private final HttpClient httpClient;
    private final Gson gson = new Gson();

    public RetrieveFoodOutlets(String url, HttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    public List<String> fetchAllFoodOutlets(String city, int maxCost) {
        List<String> foodOutlets = new ArrayList<>();
        int page = 1;
        FoodOutletsResponse response;

        do {
            response = fetchFoodOutletsByPage(city, page);
            List<String> filteredOutlets = response.getData().stream()
                    .filter(outlet -> outlet.getEstimatedCost() <= maxCost)
                    .map(FoodOutletsData::getName)
                    .toList();
            foodOutlets.addAll(filteredOutlets);
            page++;
        } while (page <= response.getTotalPages());

        return foodOutlets;
    }

    private FoodOutletsResponse fetchFoodOutletsByPage(String city, int page) {
        String urlWithPathParams = String.format("%s?city=%s&page=%d", url, city, page);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithPathParams))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), FoodOutletsResponse.class);
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new RuntimeException("Failed to fetch data from API", e);
        }
    }
}