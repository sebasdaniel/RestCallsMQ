package com.demo.sporty.controller.dto;

import jakarta.validation.constraints.NotNull;

public class StatusRequest {

    @NotNull(message = "Event Id cannot be null")
    private Integer eventId;

    @NotNull(message = "Status cannot be null")
    private Boolean status;

    public StatusRequest() {
    }

    public Integer getEventId() {
        return eventId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}