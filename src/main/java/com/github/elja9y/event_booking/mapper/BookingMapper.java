package com.github.elja9y.event_booking.mapper;

import com.github.elja9y.event_booking.dto.booking.BookingResponse;
import com.github.elja9y.event_booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "event.title", target = "eventTitle")
    @Mapping(source = "event.venue", target = "eventVenue")
    @Mapping(source = "event.eventDate", target = "eventDate")
    @Mapping(source = "user.username", target = "username")
    BookingResponse toResponse(Booking booking);
}