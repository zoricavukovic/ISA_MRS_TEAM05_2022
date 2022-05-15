package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.entities.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(value="select id from reports where reservation_id=?1", nativeQuery = true)
    Long findByReservationId(Long reservationId);
}
