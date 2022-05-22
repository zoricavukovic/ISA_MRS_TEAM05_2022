package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.dto.ComplaintDTO;
import com.example.BookingAppTeam05.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("select c from Complaint c join fetch c.reservation r " +
            "where r.id=?1")
    Optional<ComplaintDTO> findByReservationId(Long id);
}
