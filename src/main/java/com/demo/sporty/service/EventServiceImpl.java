package com.demo.sporty.service;

import com.demo.sporty.model.Event;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class EventServiceImpl implements EventService {

    private static final Map<Integer, Boolean> events = new TreeMap<>();

    @Override
    public void update(Event event) {
        if (events.containsKey(event.getEventId())) {
            events.put(event.getEventId(), event.getStatus());
        } else {
            throw new RuntimeException("The event does not exist");
        }
    }
}
