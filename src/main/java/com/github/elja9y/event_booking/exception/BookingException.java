package com.github.elja9y.event_booking.exception;

import org.springframework.http.HttpStatus;

public class BookingException extends AppException {

    public BookingException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }

    public static BookingException bookingNotFound() {
        return new BookingException("Booking not found", "BOOKING_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public static BookingException alreadyCancelled() {
        return new BookingException("This booking is already cancelled", "ALREADY_CANCELLED", HttpStatus.BAD_REQUEST);
    }

    public static BookingException alreadyBooked() {
        return new BookingException("You already have a booking for this event", "ALREADY_BOOKED", HttpStatus.BAD_REQUEST);
    }

    public static BookingException eventFull() {
        return new BookingException("This event is fully booked", "EVENT_FULL", HttpStatus.BAD_REQUEST);
    }

    public static BookingException eventIsPast() {
        return new BookingException("Cannot book a past event", "EVENT_IS_PAST", HttpStatus.BAD_REQUEST);
    }

    public static BookingException notYourBooking() {
        return new BookingException("You can only cancel your own bookings", "NOT_YOUR_BOOKING", HttpStatus.FORBIDDEN);
    }
}