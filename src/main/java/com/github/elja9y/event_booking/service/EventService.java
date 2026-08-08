package com.github.elja9y.event_booking.service;

import com.github.elja9y.event_booking.dto.event.*;
import java.util.List;

public interface EventService {
    EventResponse createEvent(EventRequest request);
    EventResponse getEventById(Long id);
    List<EventResponse> getAllEvents();
    List<EventResponse> getUpcomingEvents();
    EventResponse updateEvent(Long id, EventRequest request);
    void deleteEvent(Long id);
}