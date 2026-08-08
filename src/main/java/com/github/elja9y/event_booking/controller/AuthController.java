package com.github.elja9y.event_booking.controller;

import com.github.elja9y.event_booking.dto.auth.*;
import com.github.elja9y.event_booking.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(token);
        return ResponseEntity.ok(response);
    }
}