package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Adventure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdventureRepository extends JpaRepository<Adventure, Long> {
}
