package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.repository.ReportRepository;
import com.example.BookingAppTeam05.model.repository.ReservationRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.users.ClientService;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private ReportRepository reportRepository;
    private ReservationRepository reservationRepository;
    private BookingEntityService bookingEntityService;
    private ClientService clientService;
    private EmailService emailService;

    @Autowired
    public ReportService(ReportRepository reportRepository, ReservationRepository reservationRepository, BookingEntityService bookingEntityService, UserService userService, ClientService clientService, EmailService emailService){
        this.reportRepository = reportRepository;
        this.reservationRepository = reservationRepository;
        this.bookingEntityService = bookingEntityService;
        this.clientService = clientService;
        this.emailService = emailService;
    }

    public Report findById(Long id) {
        return this.reportRepository.findById(id).orElse(null);
    }

    public Report save(Report report) {
        return this.reportRepository.save(report);
    }

    @Transactional
    public Report addReportAndNotifyClientIfHeDidNotCome(ReportDTO reportDTO) {
        Reservation reservation = reservationRepository.getReservationById(reportDTO.getReservationId());
        if (reservation == null) return null;
        Report report = new Report(reportDTO.getComment(), reportDTO.isPenalizeClient(),!reportDTO.isClientCome() , reportDTO.isClientCome(), reservation);
        report.setVersion(0L);
        reportRepository.save(report);

        if (!report.isComeClient()) {
            String errMessage = clientService.penalizeClientFromReportAndReturnErrorMessage(report);
            try {
                emailService.notifyClientThatHeDidNotCome(report);
            } catch (Exception e) {
                return null;
            }
            if (errMessage != null) return null;
        }
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

    @Transactional
    public String giveResponse(CreatedReportReviewDTO c) {
        try {
            Report report = reportRepository.findById(c.getId()).orElse(null);
            if (report == null)
                return "Cant' find report with id: " + c.getId();
            if (report.isProcessed())
                return "This report is already processed";

            report.setAdminResponse(c.getAdminResponse());
            report.setProcessed(true);
            report.setAdminPenalizeClient(c.isAdminPenalizeClient());
            report = reportRepository.save(report);

            if (c.isAdminPenalizeClient()) {
                String err = clientService.penalizeClientFromReportAndReturnErrorMessage(report);
                if (err != null) return err;
            }
            try {
                emailService.sendEmailAsAdminResponseFromReport(c);
            } catch (Exception e) {
                return "Error happened while sending email to owner and client";
            }
            return null;
        } catch (ObjectOptimisticLockingFailureException e) {
            return "Conflict seems to have occurred, another admin has reviewed this report before you. Please refresh page and try again";
        }
    }
}
