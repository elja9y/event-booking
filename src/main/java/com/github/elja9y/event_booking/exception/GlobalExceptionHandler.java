package com.github.elja9y.event_booking.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDetails> handleAppException(AppException ex, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false),
                        ex.getErrorCode()
                ),
                ex.getStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        request.getDescription(false),
                        "INTERNAL_SERVER_ERROR"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}