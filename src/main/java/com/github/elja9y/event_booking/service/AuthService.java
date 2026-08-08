package com.github.elja9y.event_booking.service;

import com.github.elja9y.event_booking.dto.auth.*;

public interface AuthService {
    String register(RegisterRequest request);
    String login(LoginRequest request);
}