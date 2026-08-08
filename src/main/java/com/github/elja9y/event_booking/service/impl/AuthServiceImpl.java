package com.github.elja9y.event_booking.service.impl;

import com.github.elja9y.event_booking.dto.auth.*;
import com.github.elja9y.event_booking.entity.*;
import com.github.elja9y.event_booking.exception.UserException;
import com.github.elja9y.event_booking.mapper.UserMapper;
import com.github.elja9y.event_booking.repository.*;
import com.github.elja9y.event_booking.security.JwtTokenProvider;
import com.github.elja9y.event_booking.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username()))
            throw UserException.duplicatedUsername();
        if (userRepository.existsByEmail(request.email()))
            throw UserException.duplicatedEmail();

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) throw new RuntimeException("ROLE_USER not found — seed the roles table");

        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        return "Registered successfully";
    }

    @Override
    public String login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtTokenProvider.generateToken(auth);
    }
}