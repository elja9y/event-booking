package com.github.elja9y.event_booking.mapper;

import com.github.elja9y.event_booking.dto.auth.RegisterRequest;
import com.github.elja9y.event_booking.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
}