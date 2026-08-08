package com.github.elja9y.event_booking.exception;

import org.springframework.http.HttpStatus;

public class EventException extends AppException {

    public EventException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }

    public static EventException eventNotFound() {
        return new EventException("Event not found", "EVENT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public static EventException eventAlreadyPast() {
        return new EventException("Cannot modify a past event", "EVENT_ALREADY_PAST", HttpStatus.BAD_REQUEST);
    }
}