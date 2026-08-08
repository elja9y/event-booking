package com.github.elja9y.event_booking.mapper;

import com.github.elja9y.event_booking.dto.event.*;
import com.github.elja9y.event_booking.entity.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "availableSpots", expression = "java(event.getCapacity() - event.getBookedCount())")
    EventResponse toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookedCount", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Event toEvent(EventRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookedCount", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateEvent(EventRequest request, @MappingTarget Event event);
}