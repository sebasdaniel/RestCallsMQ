package com.demo.sporty.repository;

import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Repository
public class ApiEventRepository implements EventRepository {

    private static final String BASE_URL = "https://6823dee065ba058033981fa3.mockapi.io/api/v1/event/";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getEventResult(Integer eventId) throws Exception {

        String apiUrl = BASE_URL + eventId;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("URL: " + apiUrl + ", Status Code: " + response.statusCode() + ", Time: " + java.time.LocalDateTime.now());

            return response.body();
        } catch (Exception e) {
            System.err.println("Error calling URL " + apiUrl + ": " + e.getMessage() + ", Time: " + java.time.LocalDateTime.now());
            throw new Exception("Error calling URL");
        }
    }
}
