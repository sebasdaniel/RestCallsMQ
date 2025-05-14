package com.demo.sporty.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ScheduledConcurrentApiCaller {

    private static final String BASE_URL = "https://6823dee065ba058033981fa3.mockapi.io/api/v1/event/";

    private static ScheduledExecutorService scheduler;

    public void runScheduledCalls(List<Integer> eventIds) throws InterruptedException {

        if (scheduler != null) {
            scheduler.shutdown();
            scheduler.awaitTermination(15, TimeUnit.SECONDS);
        }

        scheduler = Executors.newScheduledThreadPool(eventIds.size());
        HttpClient httpClient = HttpClient.newHttpClient();
        AtomicInteger urlIndex = new AtomicInteger(0);

        Runnable apiCallTask = () -> {
            int currentIndex = urlIndex.getAndIncrement();
            if (currentIndex < eventIds.size()) {
                String apiUrl = BASE_URL + eventIds.get(currentIndex);
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(apiUrl))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Scheduled URL: " + apiUrl + ", Status Code: " + response.statusCode() + ", Time: " + java.time.LocalDateTime.now());
                    // Process the response body if needed: response.body()
                } catch (Exception e) {
                    System.err.println("Error calling scheduled URL " + apiUrl + ": " + e.getMessage() + ", Time: " + java.time.LocalDateTime.now());
                }
            }
        };

        // Schedule the task to run every 10 seconds
        scheduler.scheduleAtFixedRate(apiCallTask, 0, 10, TimeUnit.SECONDS);
    }
}
