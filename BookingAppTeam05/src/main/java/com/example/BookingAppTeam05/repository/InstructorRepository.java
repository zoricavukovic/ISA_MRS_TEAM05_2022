package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}