package com.demo.sporty.service;

import com.demo.sporty.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class EventServiceImpl implements EventService {

    private static final Map<Integer, Boolean> events = new TreeMap<>();

    private final ScheduledConcurrentApiCaller apiCallScheduler;

    @Autowired
    public EventServiceImpl(ScheduledConcurrentApiCaller apiCallScheduler) {
        this.apiCallScheduler = apiCallScheduler;
        events.put(1, false);
        events.put(2, false);
    }

    @Override
    public void update(Event event) {
        if (events.containsKey(event.getEventId())) {
            var passEvent = events.put(event.getEventId(), event.getStatus());

            if (!event.getStatus().equals(passEvent)) {
                var list = getLiveEvents();
                try {
                    apiCallScheduler.runScheduledCalls(list);
                } catch (InterruptedException e) {
                    System.err.println("Scheduler interrupted");
                }
            }
        } else {
            System.err.println("The event does not exist");
        }
    }

    private List<Integer> getLiveEvents() {
        List<Integer> result = new ArrayList<>();

        events.forEach((id, status) -> {
            if (status) {
                result.add(id);
            }
        });

        return result;
    }
}
