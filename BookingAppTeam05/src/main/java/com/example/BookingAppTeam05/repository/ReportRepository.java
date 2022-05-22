package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.entities.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(value="select id from reports where reservation_id=?1", nativeQuery = true)
    Long findByReservationId(Long reservationId);

    @Query(value="select distinct r from Report r where r.reservation.id=?1")
    Report getReportByReservationId(Long reservationId);

    @Query(value="select distinct r from Report r left join fetch r.reservation s left join fetch s.bookingEntity b left join fetch s.client c where r.processed=true and r.penalizeClient=true")
    List<Report> getAllProcessedReportsWithReservation();

    @Query(value="select distinct r from Report r left join fetch r.reservation s left join fetch s.bookingEntity b left join fetch s.client c where r.processed=false and r.penalizeClient=true")
    List<Report> getAllUnprocessedReportsWithReservation();
}
