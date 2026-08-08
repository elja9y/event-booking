package com.github.elja9y.event_booking.dto.booking;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        String eventTitle,
        String eventVenue,
        LocalDateTime eventDate,
        String username,
        LocalDateTime bookedAt,
        boolean cancelled
) {}