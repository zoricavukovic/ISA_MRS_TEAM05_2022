package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.repository.ReportRepository;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private ReportRepository reportRepository;
    private ReservationRepository reservationRepository;
    @Autowired
    public ReportService(ReportRepository reportRepository, ReservationRepository reservationRepository){
        this.reportRepository = reportRepository;
        this.reservationRepository = reservationRepository;
    }
    public Report addReport(ReportDTO reportDTO) {
        Reservation reservation = reservationRepository.getReservationById(reportDTO.getReservationId());
        if (reservation == null) return null;
        Report report = new Report(reportDTO.getComment(), reportDTO.isReward(),false , reportDTO.isClientCome(), reservation);
        reportRepository.save(report);
        return report;
    }

    public Boolean isReportedResByReservationId(Long reservationId) {
        return reportRepository.findByReservationId(reservationId) != null;
    }

    public ReportDTO getReportByReservationId(Long reservationId) {
        Report report = reportRepository.getReportByReservationId(reservationId);
        if (report == null)
            return null;
        return new ReportDTO(report);
    }
}
