package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
