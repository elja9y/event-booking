package com.github.elja9y.event_booking.dto.event;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String title,
        String description,
        String venue,
        LocalDateTime eventDate,
        int capacity,
        int bookedCount,
        int availableSpots
) {}