package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.exception.ConflictException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.NotificationByEmailException;
import com.example.BookingAppTeam05.exception.RequestAlreadyProcessedException;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.repository.ReportRepository;
import com.example.BookingAppTeam05.model.repository.ReservationRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.users.ClientService;
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
    public ReportService(ReportRepository reportRepository, ReservationRepository reservationRepository,
                         BookingEntityService bookingEntityService, ClientService clientService,
                         EmailService emailService){
        this.reportRepository = reportRepository;
        this.reservationRepository = reservationRepository;
        this.bookingEntityService = bookingEntityService;
        this.clientService = clientService;
        this.emailService = emailService;
    }

    public ReportService(){}

    public Report findById(Long id) {
        return this.reportRepository.findById(id).orElse(null);
    }

    public Report save(Report report) {
        return this.reportRepository.save(report);
    }

    @Transactional
    public void addReportAndNotifyClientIfHeDidNotCome(ReportDTO reportDTO) {
        Reservation reservation = reservationRepository.getReservationById(reportDTO.getReservationId());
        if (reservation == null)
            throw new ItemNotFoundException("Can't find reservation.");
        Report report = new Report(reportDTO.getComment(), reportDTO.isPenalizeClient(),!reportDTO.isClientCome() , reportDTO.isClientCome(), reservation);
        report.setVersion(0L);
        report = reportRepository.save(report);

        if (!report.isComeClient()) {
            clientService.penalizeClientFromReportAndReturnErrorMessage(report);
            try {
                emailService.notifyClientThatHeDidNotCome(report);
            } catch (InterruptedException e) {
                throw new NotificationByEmailException("Error happened while sending email about report.");
            }
        }
    }

    public Boolean isReportedResByReservationId(Long reservationId) {
        return reportRepository.findByReservationId(reservationId) != null;
    }

    public ReportDTO getReportByReservationId(Long reservationId) {
        Report report = reportRepository.getReportByReservationId(reservationId);
        if (report == null)
            throw new ItemNotFoundException("Can't find report for this reservation.");
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

    public List<CreatedReportReviewDTO> getListOfCreatedReports(String type) {
        List<CreatedReportReviewDTO> retVal = null;
        if (type.equals("processed"))
            retVal = getAllProcessedReportReviewDTOs();
        else if (type.equals("unprocessed"))
            retVal = getAllUnprocessedReportReviewDTOs();

        if (retVal == null)
            throw new ItemNotFoundException("Can't find created reports for type " + type);
        return retVal;
    }

    @Transactional
    public void giveResponse(CreatedReportReviewDTO c) {
        try {
            Report report = reportRepository.findById(c.getId()).orElse(null);
            if (report == null)
                throw new ItemNotFoundException("Can't find report with id: " + c.getId());
            if (report.isProcessed())
                throw new RequestAlreadyProcessedException("This report is already processed.");
            report.setProcessed(true);
            report.setAdminResponse(c.getAdminResponse());
            report.setAdminPenalizeClient(c.isAdminPenalizeClient());
            report = reportRepository.save(report);
            if (c.isAdminPenalizeClient())
                clientService.penalizeClientFromReportAndReturnErrorMessage(report);
            try {
                emailService.sendEmailAsAdminResponseFromReport(c);
            } catch (InterruptedException e) {
                throw new NotificationByEmailException("Error happened while sending email about report.");
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Conflict seems to have occurred, another admin has reviewed this report before you. Please refresh page and try again");
        }
    }
}
