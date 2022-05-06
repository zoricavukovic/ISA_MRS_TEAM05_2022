package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.repository.*;
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
    private EmailService emailService;
    private SubscriberRepository subscriberRepository;
    private UserRepository userRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, CottageRepository cottageRepository,
                              ClientRepository clientRepository, BookingEntityRepository bookingEntityRepository,
                              EmailService emailService, SubscriberRepository subscriberRepository, UserRepository userRepository){
        this.reservationRepository = reservationRepository;
        this.cottageRepository = cottageRepository;
        this.clientRepository = clientRepository;
        this.bookingEntityRepository = bookingEntityRepository;
        this.emailService = emailService;
        this.subscriberRepository = subscriberRepository;
        this.userRepository = userRepository;
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

    public List<Reservation> findAllReservationsWithClientsForEntityId(Long id) {
        return this.reservationRepository.findAllReservationsWithClientsForEntityId(id);
    }

    public List<Reservation> findAllFastReservationsForEntityid(Long id) {
        return this.reservationRepository.findAllFastReservationsForEntityId(id);
    }
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
            if (reservationDTO.getStartDate().isBefore(LocalDateTime.now())) return null;
            res.setStartDate(reservationDTO.getStartDate());
            res.setCost(reservationDTO.getCost());
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
            List<Long> subscribersIds = subscriberRepository.findAllSubscribersForEntityId(res.getBookingEntity().getId());
            List<Client> subscribers = new ArrayList<>();
            for (Long s: subscribersIds){
                Client client = (Client) userRepository.getUserById(s);
                subscribers.add(client);
            }
            if (sendMail(res, subscribers).equals("error")) return null;
            return res;
        }catch (OptimisticLockException e){
            System.out.println("EXCEPTION HAS HAPPENED!!!!");
        }
        return null;
    }

    private String sendMail(Reservation reservation, List<Client> subscribers){

        //slanje emaila
        try {
            System.out.println("Thread id: " + Thread.currentThread().getId());
            emailService.sendNotificaitionAsync(reservation, subscribers);
        }catch( Exception e ){
            System.out.println("Greska prilikom slanja emaila: " + e.getMessage());
            return "error";
        }

        return "success";
    }


    public List<Reservation> findAllActiveReservationsForEntityid(Long id) {
        List<Reservation> reservations = reservationRepository.findAllRegularAndFastReservationsForEntityId(id);
        List<Reservation> retVal = new ArrayList<>();
        for (Reservation r : reservations) {
            if (!r.isFinished())
                retVal.add(r);
        }
        return retVal;
    }
}
