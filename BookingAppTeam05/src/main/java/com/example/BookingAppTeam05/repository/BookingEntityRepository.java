package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingEntityRepository extends JpaRepository<BookingEntity, Long> {
}
