package com.github.elja9y.event_booking.repository;

import com.github.elja9y.event_booking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}