package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAllActiveReservationsForCottage(Long cottageId){return reservationRepository.findAllActiveReservationsForCottage(cottageId);}
}
