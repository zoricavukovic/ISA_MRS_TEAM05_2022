package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
