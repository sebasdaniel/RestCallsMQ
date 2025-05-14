package com.demo.sporty.model;

public class Event {

    private Integer eventId;
    private Boolean status;

    public Event() {
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

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", status=" + status +
                '}';
    }
}
