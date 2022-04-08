package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}
