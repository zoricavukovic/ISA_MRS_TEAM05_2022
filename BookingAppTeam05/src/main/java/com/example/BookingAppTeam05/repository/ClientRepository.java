package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}