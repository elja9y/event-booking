package com.github.elja9y.event_booking.config;

import com.github.elja9y.event_booking.security.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@Configuration
public class SpringSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtAuthenticationFilter jwtFilter;

    public SpringSecurityConfig(UserDetailsService userDetailsService,
                                JwtAuthenticationEntryPoint entryPoint,
                                JwtAuthenticationFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.entryPoint = entryPoint;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}