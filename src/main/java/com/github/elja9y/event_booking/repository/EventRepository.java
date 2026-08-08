package com.github.elja9y.event_booking.repository;

import com.github.elja9y.event_booking.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateAfter(LocalDateTime date);
    List<Event> findByVenueContainingIgnoreCase(String venue);
}