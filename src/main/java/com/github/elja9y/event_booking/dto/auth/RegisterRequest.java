package com.github.elja9y.event_booking.dto.auth;

public record RegisterRequest(
        String name,
        String username,
        String email,
        String password
) {}