package com.demo.sporty.service;

import com.demo.sporty.repository.EventRepository;
import com.demo.sporty.service.producer.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledConcurrentApiCaller {

    private static final String MESSAGE_KEY = "result";

    private static ScheduledExecutorService scheduler;

    @Autowired
    private EventRepository repository;

    @Autowired
    private ProducerService producer;

    public void runScheduledCalls(List<Integer> eventIds) throws InterruptedException {

        if (scheduler != null) {
            scheduler.shutdown();
            scheduler.awaitTermination(15, TimeUnit.SECONDS);
        }

        scheduler = Executors.newScheduledThreadPool(eventIds.size());

        for (Integer id : eventIds) {
            Runnable apiCallTask = () -> {
                try {
                    var eventResult = repository.getEventResult(id);
                    producer.send(MESSAGE_KEY, eventResult);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            };

            // Schedule the task to run every 10 seconds
            scheduler.scheduleAtFixedRate(apiCallTask, 0, 10, TimeUnit.SECONDS);
        }
    }
}
