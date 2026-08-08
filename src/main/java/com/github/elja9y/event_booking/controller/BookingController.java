package com.github.elja9y.event_booking.controller;

import com.github.elja9y.event_booking.dto.booking.BookingResponse;
import com.github.elja9y.event_booking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingService bookingService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{eventId}")
    public ResponseEntity<BookingResponse> bookEvent(@PathVariable Long eventId,
                                                     Authentication authentication) {
        return new ResponseEntity<>(
                bookingService.bookEvent(eventId, authentication.getName()),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id,
                                                         Authentication authentication) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, authentication.getName()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getMyBookings(authentication.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}