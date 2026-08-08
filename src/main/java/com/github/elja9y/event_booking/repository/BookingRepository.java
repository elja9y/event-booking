package com.github.elja9y.event_booking.repository;

import com.github.elja9y.event_booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    boolean existsByUserIdAndEventIdAndCancelledFalse(Long userId, Long eventId);
    List<Booking> findByEventId(Long eventId);
}