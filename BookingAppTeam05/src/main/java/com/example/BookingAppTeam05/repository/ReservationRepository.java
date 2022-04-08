package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
