package com.demo.sporty.controller;

import com.demo.sporty.controller.dto.StatusRequest;
import com.demo.sporty.model.Event;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events/status")
    public void updateStatus(@Valid @RequestBody StatusRequest request) {
        Event event = mapToEvent(request);
        eventService.update(event);
    }

    private Event mapToEvent(StatusRequest request) {
        Event event = new Event();
        event.setEventId(request.getEventId());
        event.setStatus(request.getStatus());

        return event;
    }
}
