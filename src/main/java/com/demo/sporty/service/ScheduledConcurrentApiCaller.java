package com.demo.sporty.service;

import com.demo.sporty.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EventRepository repository;

    public void runScheduledCalls(List<Integer> eventIds) throws InterruptedException {

        if (scheduler != null) {
            scheduler.shutdown();
            scheduler.awaitTermination(15, TimeUnit.SECONDS);
        }

        scheduler = Executors.newScheduledThreadPool(eventIds.size());
        AtomicInteger urlIndex = new AtomicInteger(0);

        Runnable apiCallTask = () -> {
            int currentIndex = urlIndex.getAndIncrement();
            if (currentIndex < eventIds.size()) {

                try {
                    var eventResult = repository.getEventResult(eventIds.get(currentIndex));
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        };

        // Schedule the task to run every 10 seconds
        scheduler.scheduleAtFixedRate(apiCallTask, 0, 10, TimeUnit.SECONDS);
    }
}
