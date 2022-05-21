package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.ComplaintReviewDTO;
import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Complaint;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.repository.ComplaintRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComplaintService {

    private ComplaintRepository complaintRepository;
    private BookingEntityService bookingEntityService;
    private EmailService emailService;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, BookingEntityService bookingEntityService, EmailService emailService) {
        this.complaintRepository = complaintRepository;
        this.bookingEntityService = bookingEntityService;
        this.emailService = emailService;
    }

    public List<ComplaintReviewDTO> getAllUnprocessedComplaintReviewDTOs() {
        List<Complaint> complaints = complaintRepository.getAllUnprocessedComplaintsWithReservation();
        return createdComplaintReviewDTOSFromComplaintList(complaints);
    }

    private List<ComplaintReviewDTO> createdComplaintReviewDTOSFromComplaintList(List<Complaint> complaints) {
        List<ComplaintReviewDTO> retVal = new ArrayList<>();
        for (Complaint complaint : complaints) {
            ReservationDTO reservationDTO = new ReservationDTO(complaint.getReservation());
            reservationDTO.setClient(new ClientDTO(complaint.getReservation().getClient()));
            reservationDTO.setBookingEntity(new BookingEntityDTO(complaint.getReservation().getBookingEntity()));
            UserDTO ownerDTO = new UserDTO(bookingEntityService.getOwnerOfEntityId(complaint.getReservation().getBookingEntity().getId()));
            ComplaintReviewDTO c = new ComplaintReviewDTO(complaint, reservationDTO, ownerDTO);
            retVal.add(c);
        }
        return retVal;
    }

    public List<ComplaintReviewDTO> getAllProcessedComplaintReviewDTOs() {
        List<Complaint> complaints = complaintRepository.getAllProcessedComplaintsWithReservation();
        return createdComplaintReviewDTOSFromComplaintList(complaints);
    }

    @Transactional
    public String giveResponse(ComplaintReviewDTO c) {
        Complaint complaint = complaintRepository.findById(c.getId()).orElse(null);
        if (complaint == null)
            return "Cant' find complaint with id: " + c.getId();
        complaint.setAdminResponse(c.getAdminResponse());
        complaint.setProcessed(true);
        complaint = complaintRepository.save(complaint);

        try {
            emailService.sendEmailAsAdminResponseFromComplaint(c);
        } catch (Exception e) {
            return "Error happened while sending email to owner and client";
        }
        return null;
    }
}
