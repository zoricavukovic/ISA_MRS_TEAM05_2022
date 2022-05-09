package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.users.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    Optional<Client> findById(Long aLong);
}
