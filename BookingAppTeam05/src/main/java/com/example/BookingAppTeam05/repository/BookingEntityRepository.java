package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingEntityRepository extends JpaRepository<BookingEntity, Long> {

}
