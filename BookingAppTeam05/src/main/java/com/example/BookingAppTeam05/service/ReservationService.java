package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.repository.CottageRepository;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private CottageRepository cottageRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, CottageRepository cottageRepository){
        this.reservationRepository = reservationRepository;
        this.cottageRepository = cottageRepository;
    }

    public List<Reservation> findAllActiveReservationsForCottage(Long cottageId){return reservationRepository.findAllActiveReservationsForCottage(cottageId);}

    public List<Reservation> getReservationsByCottageOwnerId(Long ownerId) {
        List<Cottage> cottages = cottageRepository.getCottagesByOwnerId(ownerId);
        List<Reservation> reservations = new ArrayList<>();
        for (Cottage cottage: cottages){
            List<Reservation> reservationsByCottageId = getReservationsByCottageId(cottage.getId());
            for (Reservation reservation: reservationsByCottageId){
                reservation.setBookingEntity(cottage);
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> getReservationsByCottageId(Long cottageId){return reservationRepository.getReservationsByCottageId(cottageId);}

    public List<Reservation> getFastReservationsByBookingEntityId(Long bookingEntityId) {
        List<Reservation> allFastRes = reservationRepository.getFastReservationsByBookingEntityId(bookingEntityId);
        System.out.println("caocao" + " " + allFastRes.size());
        List<Reservation> activeFastRes = new ArrayList<>();
        for (Reservation r : allFastRes){
            System.out.println(allFastRes.size());
            if ((r.getStartDate()).isAfter(LocalDateTime.now())) activeFastRes.add(r);
        }
        return activeFastRes;
    }
}
