package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.ComplaintDTO;
import com.example.BookingAppTeam05.model.Complaint;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.repository.ComplaintRepository;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

    private ComplaintRepository complaintRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, ReservationRepository reservationRepository) {
        this.complaintRepository = complaintRepository;
        this.reservationRepository = reservationRepository;
    }

    public ComplaintDTO getComplaintByReservationId(Long id) {
        return complaintRepository.findByReservationId(id).orElse(null);
    }

    public Complaint createComplaint(ComplaintDTO complaintDTO) {
        Complaint complaint = new Complaint();
        complaint.setDescription(complaintDTO.getDescription());
        Reservation res = reservationRepository.findById(complaintDTO.getReservation().getId()).orElse(null);
        complaint.setReservation(res);
        this.complaintRepository.save(complaint);
        return complaint;
    }
}
