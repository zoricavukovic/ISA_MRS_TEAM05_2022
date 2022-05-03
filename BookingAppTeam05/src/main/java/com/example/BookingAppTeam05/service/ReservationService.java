package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.repository.BookingEntityRepository;
import com.example.BookingAppTeam05.repository.ClientRepository;
import com.example.BookingAppTeam05.repository.CottageRepository;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private CottageRepository cottageRepository;
    private ClientRepository clientRepository;
    private BookingEntityRepository bookingEntityRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, CottageRepository cottageRepository,
                              ClientRepository clientRepository, BookingEntityRepository bookingEntityRepository){
        this.reservationRepository = reservationRepository;
        this.cottageRepository = cottageRepository;
        this.clientRepository = clientRepository;
        this.bookingEntityRepository = bookingEntityRepository;
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

    public Reservation addFastReservation(ReservationDTO reservationDTO){
        try{
            Reservation res = new Reservation();
            res.setStartDate(reservationDTO.getStartDate());
            res.setCanceled(false);
            res.setFastReservation(true);
            res.setNumOfDays(reservationDTO.getNumOfDays());
            res.setNumOfPersons(reservationDTO.getNumOfPersons());
            res.setAdditionalServices(reservationDTO.getAdditionalServices());
            BookingEntityDTO entityDTO = reservationDTO.getBookingEntity();
            if (entityDTO == null) return null;
            BookingEntity entity = bookingEntityRepository.getEntityById(entityDTO.getId());
            if (entity == null) return null;
            res.setBookingEntity(entity);
            res.setVersion(1);
            reservationRepository.save(res);
            return res;
        }catch (OptimisticLockException e){
            System.out.println("EXCEPTION HAS HAPPENED!!!!");
        }
        return null;
    }

}
