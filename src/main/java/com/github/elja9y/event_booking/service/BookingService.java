package com.github.elja9y.event_booking.service;

import com.github.elja9y.event_booking.dto.booking.BookingResponse;
import java.util.List;

public interface BookingService {
    BookingResponse bookEvent(Long eventId, String username);
    BookingResponse cancelBooking(Long bookingId, String username);
    List<BookingResponse> getMyBookings(String username);
    List<BookingResponse> getAllBookings();
}