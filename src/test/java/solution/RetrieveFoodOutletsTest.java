package solution;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RetrieveFoodOutletsTest {
    private RetrieveFoodOutlets retrieveFoodOutlets;
    private HttpClient mockHttpClient;
    private HttpResponse mockResponse;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        mockHttpClient = Mockito.mock(HttpClient.class);
        mockResponse = Mockito.mock(HttpResponse.class);

        when(mockHttpClient.send(any(HttpRequest.class), any())).thenReturn(mockResponse);
        String mockJsonResponse = """
                {
                    "page": 1,
                    "per_page": 10,
                    "total": 2,
                    "total_pages": 1,
                    "data": [
                        {
                            "id": 1,
                            "name": "Pizza Hut",
                            "city": "Seattle",
                            "estimated_cost": 25,
                            "user_rating": {
                                "average_rating": 4.5,
                                "votes": 100
                            }
                        },
                        {
                            "id": 2,
                            "name": "McDonald's",
                            "city": "Seattle",
                            "estimated_cost": 15,
                            "user_rating": {
                                "average_rating": 4.0,
                                "votes": 50
                            }
                        }
                    ]
                }
                """;
        when(mockResponse.body()).thenReturn(mockJsonResponse);

        retrieveFoodOutlets = new RetrieveFoodOutlets("https://whatever/food_outlets", mockHttpClient);
    }

    @Test
    void testFetchFoodOutlets_FilterByCost() {
        List<String> result = retrieveFoodOutlets.fetchAllFoodOutlets("Seattle", 20);

        List<String> expected = List.of("McDonald's");

        assertEquals(expected, result, "Filtered results do not match expected output.");
    }

    @Test
    void testFetchFoodOutlets_NoFilter() {
        List<String> result = retrieveFoodOutlets.fetchAllFoodOutlets("Seattle", 100);

        List<String> expected = List.of("Pizza Hut", "McDonald's");

        assertEquals(expected, result, "Expected all food outlets when no filtering is applied.");
    }

    @Test
    void testFetchFoodOutlets_EmptyResponse() {
        String emptyJsonResponse = """
                {
                    "page": 1,
                    "per_page": 10,
                    "total": 0,
                    "total_pages": 1,
                    "data": []
                }
                """;
        when(mockResponse.body()).thenReturn(emptyJsonResponse);

        List<String> result = retrieveFoodOutlets.fetchAllFoodOutlets("Seattle", 50);

        assertTrue(result.isEmpty(), "Expected an empty list but got results.");
    }

    @Test
    void testFetchFoodOutlets_InvalidCity() {
        String invalidCityResponse = """
                {
                    "page": 1,
                    "per_page": 10,
                    "total": 0,
                    "total_pages": 1,
                    "data": []
                }
                """;
        when(mockResponse.body()).thenReturn(invalidCityResponse);

        List<String> result = retrieveFoodOutlets.fetchAllFoodOutlets("InvalidCity", 50);

        assertTrue(result.isEmpty(), "Expected an empty list for an invalid city.");
    }

    @Test
    void testFetchFoodOutlets_Pagination() throws IOException, InterruptedException {
        HttpResponse page1Response = mock(HttpResponse.class);
        when(page1Response.body()).thenReturn("""
                    {
                        "page": 1,
                        "per_page": 1,
                        "total": 2,
                        "total_pages": 2,
                        "data": [
                            {
                                "id": 1,
                                "name": "Pizza Hut",
                                "city": "Seattle",
                                "estimated_cost": 25,
                                "user_rating": {
                                    "average_rating": 4.5,
                                    "votes": 100
                                }
                            }
                        ]
                    }
                """);

        HttpResponse page2Response = mock(HttpResponse.class);
        when(page2Response.body()).thenReturn("""
                    {
                        "page": 2,
                        "per_page": 1,
                        "total": 2,
                        "total_pages": 2,
                        "data": [
                            {
                                "id": 2,
                                "name": "McDonald's",
                                "city": "Seattle",
                                "estimated_cost": 15,
                                "user_rating": {
                                    "average_rating": 4.0,
                                    "votes": 50
                                }
                            }
                        ]
                    }
                """);

        when(mockHttpClient.send(any(HttpRequest.class), any()))
                .thenReturn(page1Response)
                .thenReturn(page2Response);

        List<String> result = retrieveFoodOutlets.fetchAllFoodOutlets("Seattle", 100);
        List<String> expected = List.of("Pizza Hut", "McDonald's");

        assertEquals(expected, result, "Pagination handling did not return all expected outlets.");
    }
}