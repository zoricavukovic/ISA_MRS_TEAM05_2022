package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.ComplaintDTO;
import com.example.BookingAppTeam05.exception.ConflictException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.NotificationByEmailException;
import com.example.BookingAppTeam05.exception.RequestAlreadyProcessedException;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.model.Complaint;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.repository.ComplaintRepository;
import com.example.BookingAppTeam05.dto.ComplaintReviewDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComplaintService {

    private ComplaintRepository complaintRepository;
    private ReservationService reservationService;
    private BookingEntityService bookingEntityService;
    private EmailService emailService;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, ReservationService reservationService, BookingEntityService bookingEntityService, EmailService emailService) {
        this.complaintRepository = complaintRepository;
        this.reservationService = reservationService;
        this.bookingEntityService = bookingEntityService;
        this.emailService = emailService;
    }

    public ComplaintService(){}

    public ComplaintDTO getComplaintByReservationId(Long id) {
        ComplaintDTO complaintDTO = complaintRepository.findByReservationId(id).orElse(null);
        if (complaintDTO == null)
            throw new ItemNotFoundException("Complaint for this reservation can't be find.");
        return complaintDTO;
    }
    
    public Complaint findById(Long id) {
        return this.complaintRepository.findById(id).orElse(null);
    }

    public Complaint save(Complaint complaint) {
        return this.complaintRepository.save(complaint);
    }

    public Complaint createComplaint(ComplaintDTO complaintDTO) {
        Complaint complaint = new Complaint();
        complaint.setDescription(complaintDTO.getDescription());
        if (complaintDTO.getReservation() == null){
            throw new CreateItemException("Can't create complaint without set reservation.");
        }
        Reservation res = reservationService.findById(complaintDTO.getReservation().getId());
        if (complaintDTO.getReservation() == null)
            throw new ItemNotFoundException("Can't find reservation with id: " + complaintDTO.getReservation().getId() + ".");
        complaint.setReservation(res);
        complaint.setVersion(0L);
        this.complaintRepository.save(complaint);
        return complaint;
    }

    public List<ComplaintReviewDTO> getAllUnprocessedComplaintReviewDTOs() {
        List<Complaint> complaints = complaintRepository.getAllUnprocessedComplaintsWithReservation();
        return createdComplaintReviewDTOSFromComplaintList(complaints);
    }

    public List<ComplaintReviewDTO> getAllProcessedComplaintReviewDTOs() {
        List<Complaint> complaints = complaintRepository.getAllProcessedComplaintsWithReservation();
        return createdComplaintReviewDTOSFromComplaintList(complaints);
    }

    public List<ComplaintReviewDTO> getListOfCreatedReports(String type) {
        List<ComplaintReviewDTO> retVal = null;
        if (type.equals("processed"))
            retVal = getAllProcessedComplaintReviewDTOs();
        else if (type.equals("unprocessed"))
            retVal = getAllUnprocessedComplaintReviewDTOs();

        if (retVal == null)
            throw new ItemNotFoundException("Can't find created reports for type " + type);
        return retVal;
    }

    @Transactional
    public void giveResponse(ComplaintReviewDTO c) {
        try {
            Complaint complaint = complaintRepository.findById(c.getId()).orElse(null);
            if (complaint == null)
                throw new ItemNotFoundException("Can't find complaint with id: " + c.getId());
            if (complaint.isProcessed())
                throw new RequestAlreadyProcessedException("This complaint is already processed.");

            complaint.setAdminResponse(c.getAdminResponse());
            complaint.setProcessed(true);
            complaintRepository.save(complaint);

            try {
                emailService.sendEmailAsAdminResponseFromComplaint(c);
            } catch (InterruptedException e) {
                throw new NotificationByEmailException("Error happened while sending email to owner and client about complaint processed");
            }
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Conflict seems to have occurred, another admin has reviewed this complaint before you. Please refresh page and try again");
        }
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
}
