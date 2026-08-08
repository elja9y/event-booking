package com.github.elja9y.event_booking.dto.auth;

public record LoginRequest(
        String usernameOrEmail,
        String password
) {}