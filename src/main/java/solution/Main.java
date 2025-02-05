package solution;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        String BASE_URL = "https://jsonmock.hackerrank.com/api/food_outlets";
        String city = "Seattle";
        int maxCost = 160;

        try(HttpClient client = HttpClient.newHttpClient()) {
            RetrieveFoodOutlets retrieveFoodOutlets = new RetrieveFoodOutlets(BASE_URL, client);

            System.out.println(retrieveFoodOutlets.fetchAllFoodOutlets(city, maxCost));
        }
    }
}