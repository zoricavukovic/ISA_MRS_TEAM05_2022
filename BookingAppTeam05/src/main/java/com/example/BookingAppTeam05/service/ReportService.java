package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.repository.ReportRepository;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private ReportRepository reportRepository;
    private ReservationRepository reservationRepository;
    private BookingEntityService bookingEntityService;

    @Autowired
    public ReportService(ReportRepository reportRepository, ReservationRepository reservationRepository, BookingEntityService bookingEntityService){
        this.reportRepository = reportRepository;
        this.reservationRepository = reservationRepository;
        this.bookingEntityService = bookingEntityService;
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

    public List<CreatedReportReviewDTO> getAllUnprocessedReportReviewDTOs() {
        List<Report> reports = reportRepository.getAllUnprocessedReportsWithReservation();
        return createdReportReviewDTOSFromReportList(reports);
    }

    public List<CreatedReportReviewDTO> getAllProcessedReportReviewDTOs() {
        List<Report> reports = reportRepository.getAllProcessedReportsWithReservation();
        return createdReportReviewDTOSFromReportList(reports);
    }

    private List<CreatedReportReviewDTO> createdReportReviewDTOSFromReportList(List<Report> reports) {
        List<CreatedReportReviewDTO> retVal = new ArrayList<>();
        for (Report report : reports) {
            if (report.getReservation().isFastReservation())
                continue;
            ReservationDTO reservationDTO = new ReservationDTO(report.getReservation());
            reservationDTO.setClient(new ClientDTO(report.getReservation().getClient()));
            reservationDTO.setBookingEntity(new BookingEntityDTO(report.getReservation().getBookingEntity()));
            UserDTO ownerDTO = new UserDTO(bookingEntityService.getOwnerOfEntityId(report.getReservation().getBookingEntity().getId()));
            CreatedReportReviewDTO c = new CreatedReportReviewDTO(report, reservationDTO, ownerDTO);
            retVal.add(c);
        }
        return retVal;
    }
}
