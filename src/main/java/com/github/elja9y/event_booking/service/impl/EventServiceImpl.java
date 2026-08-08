package com.github.elja9y.event_booking.service.impl;

import com.github.elja9y.event_booking.dto.event.*;
import com.github.elja9y.event_booking.entity.Event;
import com.github.elja9y.event_booking.exception.EventException;
import com.github.elja9y.event_booking.mapper.EventMapper;
import com.github.elja9y.event_booking.repository.EventRepository;
import com.github.elja9y.event_booking.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private EventMapper eventMapper;

    @Override
    public EventResponse createEvent(EventRequest request) {
        Event event = eventMapper.toEvent(request);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    public EventResponse getEventById(Long id) {
        return eventMapper.toResponse(getEventEntityById(id));
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Override
    public List<EventResponse> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDateTime.now()).stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Override
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = getEventEntityById(id);

        if (event.getEventDate().isBefore(LocalDateTime.now()))
            throw EventException.eventAlreadyPast();

        eventMapper.updateEvent(request, event);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.delete(getEventEntityById(id));
    }

    // package-private so BookingService can reuse it
    public Event getEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(EventException::eventNotFound);
    }
}