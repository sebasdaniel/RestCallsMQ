package com.demo.sporty.service;

import com.demo.sporty.model.Event;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EventServiceImplTest {

    private final Boolean LIVE_STATUS = true;
    private final Boolean NOT_LIVE_STATUS = false;

    private final ScheduledConcurrentApiCaller apiCallScheduler = Mockito.mock(ScheduledConcurrentApiCaller.class);

    private final EventServiceImpl eventService = new EventServiceImpl(apiCallScheduler);


    @Test
    void update_existingEvent_statusChangesToLive_shouldCallScheduler() throws InterruptedException {
        Event eventToUpdate = new Event();
        eventToUpdate.setEventId(1);
        eventToUpdate.setStatus(LIVE_STATUS);
        List<Integer> expectedLiveEvents = List.of(1);

        eventService.update(eventToUpdate);

        verify(apiCallScheduler, times(1)).runScheduledCalls(expectedLiveEvents);
    }

    @Test
    void update_existingEvent_statusChangesToNotLive_shouldCallScheduler() throws InterruptedException {
        Event eventToUpdate = new Event();
        eventToUpdate.setEventId(1);
        eventToUpdate.setStatus(LIVE_STATUS);

        eventService.update(eventToUpdate);

        eventToUpdate.setStatus(NOT_LIVE_STATUS);

        eventService.update(eventToUpdate);

        List<Integer> expectedLiveEvents = List.of(1);
        verify(apiCallScheduler, times(1)).runScheduledCalls(expectedLiveEvents);
        verify(apiCallScheduler, times(1)).runScheduledCalls(List.of());
    }

    @Test
    void update_existingEvent_statusDoesNotChange_shouldNotCallScheduler() throws InterruptedException {
        Event eventToUpdate = new Event();
        eventToUpdate.setEventId(1);
        eventToUpdate.setStatus(NOT_LIVE_STATUS);

        eventService.update(eventToUpdate);

        verify(apiCallScheduler, never()).runScheduledCalls(anyList());
    }

    @Test
    void update_nonExistingEvent_shouldNotCallSchedulerAndPrintError() throws InterruptedException {
        Event eventToUpdate = new Event();
        eventToUpdate.setEventId(3);
        eventToUpdate.setStatus(LIVE_STATUS);

        eventService.update(eventToUpdate);

        verify(apiCallScheduler, never()).runScheduledCalls(anyList());
    }

    @Test
    void update_schedulerThrowsInterruptedException_shouldDoNothing() throws InterruptedException {
        Event eventToUpdate = new Event();
        eventToUpdate.setEventId(1);
        eventToUpdate.setStatus(LIVE_STATUS);

        doThrow(new InterruptedException("Scheduler interrupted for testing")).when(apiCallScheduler).runScheduledCalls(anyList());

        assertDoesNotThrow(() -> eventService.update(eventToUpdate));
    }
}