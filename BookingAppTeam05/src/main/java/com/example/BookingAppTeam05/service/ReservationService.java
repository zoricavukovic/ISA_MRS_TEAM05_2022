package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.dto.ReservationForClientDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.repository.AdditionalServiceRepository;
import com.example.BookingAppTeam05.model.repository.ReservationRepository;
import com.example.BookingAppTeam05.model.repository.SubscriberRepository;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.repository.entities.AdventureRepository;
import com.example.BookingAppTeam05.model.repository.entities.BookingEntityRepository;
import com.example.BookingAppTeam05.model.repository.entities.CottageRepository;
import com.example.BookingAppTeam05.model.repository.entities.ShipRepository;
import com.example.BookingAppTeam05.model.repository.users.ClientRepository;
import com.example.BookingAppTeam05.model.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private CottageRepository cottageRepository;
    private ShipRepository shipRepository;
    private AdventureRepository adventureRepository;
    private ClientRepository clientRepository;
    private BookingEntityRepository bookingEntityRepository;
    private EmailService emailService;
    private SubscriberRepository subscriberRepository;
    private UserRepository userRepository;
    private AdditionalServiceRepository additionalServiceRepository;

    private LoyaltyProgramService loyaltyProgramService;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository, CottageRepository cottageRepository,
                              ShipRepository shipRepository, AdventureRepository adventureRepository,
                              ClientRepository clientRepository, BookingEntityRepository bookingEntityRepository,
                              EmailService emailService, SubscriberRepository subscriberRepository, UserRepository userRepository,
                              AdditionalServiceRepository additionalServiceRepository
                              ){
        this.reservationRepository = reservationRepository;
        this.cottageRepository = cottageRepository;
        this.shipRepository = shipRepository;
        this.adventureRepository = adventureRepository;
        this.clientRepository = clientRepository;
        this.bookingEntityRepository = bookingEntityRepository;
        this.emailService = emailService;
        this.subscriberRepository = subscriberRepository;
        this.userRepository = userRepository;
        this.additionalServiceRepository = additionalServiceRepository;
    }



    public List<Reservation> findAllActiveReservationsForEntity(Long entityId){return reservationRepository.findAllActiveReservationsForBookingEntity(entityId);}

    public List<Reservation> getReservationsByOwnerId(Long ownerId, String type) {
        List<Reservation> reservations = new ArrayList<>();

        switch (type) {
            case "COTTAGE": {
                List<Cottage> entities = cottageRepository.getCottagesByOwnerId(ownerId);
                for (BookingEntity entity : entities)
                    addingReservations(reservations, entity);
                break;
            }
            case "SHIP": {
                List<Ship> entities = shipRepository.getShipsByOwnerId(ownerId);
                for (BookingEntity entity : entities)
                    addingReservations(reservations, entity);
                break;
            }
            case "ADVENTURE": {
                List<Adventure> entities = adventureRepository.getAdventuresForOwnerId(ownerId);
                for (BookingEntity entity : entities)
                    addingReservations(reservations, entity);
                break;
            }
        }
        return reservations;
    }

    public List<ReservationDTO> filterReservation(List<ReservationDTO> reservationDTOs, String name, String time){
        List<ReservationDTO> filteredReservationDTOs = new ArrayList<>();
        for (ReservationDTO reservationDTO: reservationDTOs){
            BookingEntityDTO bookingEntity = reservationDTO.getBookingEntity();
            if (bookingEntity!= null){
                if (name.equals("ALL") && time.equals("ALL")){
                    filteredReservationDTOs.add(reservationDTO);
                }
                else if (!name.equals("ALL") && time.equals("ALL")){
                    if (bookingEntity.getName().equals(name)){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (name.equals("ALL") && time.equals("CANCELED")){
                    if (reservationDTO.isCanceled()){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (name.equals("ALL") && time.equals("FINISHED")){
                    if (!reservationDTO.isCanceled() && !(reservationDTO.getStartDate().plusDays(reservationDTO.getNumOfDays())).isAfter(LocalDateTime.now())){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (name.equals("ALL") && time.equals("NOT_STARTED")){
                    if (!reservationDTO.isCanceled() && (reservationDTO.getStartDate()).isAfter(LocalDateTime.now())){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (name.equals("ALL") && time.equals("STARTED")){
                    if (!reservationDTO.isCanceled() && (reservationDTO.getStartDate()).isBefore(LocalDateTime.now()) && (reservationDTO.getStartDate().plusDays(reservationDTO.getNumOfDays())).isAfter(LocalDateTime.now())){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (!name.equals("ALL") && time.equals("CANCELED")){
                    if (bookingEntity.getName().equals(name) && reservationDTO.isCanceled()){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (!name.equals("ALL") && time.equals("FINISHED")){
                    if (bookingEntity.getName().equals(name) && !reservationDTO.isCanceled() && !(reservationDTO.getStartDate().plusDays(reservationDTO.getNumOfDays())).isAfter(LocalDateTime.now())){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (!name.equals("ALL") && time.equals("NOT_STARTED")){
                    if (bookingEntity.getName().equals(name) && !reservationDTO.isCanceled() && (reservationDTO.getStartDate()).isAfter(LocalDateTime.now())){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
                else if (!name.equals("ALL") && time.equals("STARTED")){
                    if (bookingEntity.getName().equals(name) && !reservationDTO.isCanceled() && (reservationDTO.getStartDate()).isBefore(LocalDateTime.now()) && (reservationDTO.getStartDate().plusDays(reservationDTO.getNumOfDays())).isAfter(LocalDateTime.now())){
                        filteredReservationDTOs.add(reservationDTO);
                    }
                }
            }
        }
        return filteredReservationDTOs;
    }

    private void addingReservations(List<Reservation> reservations, BookingEntity entity) {
        List<Reservation> reservationsByCottageId = getReservationsByEntityId(entity.getId());
        for (Reservation reservation : reservationsByCottageId) {
            reservation.setBookingEntity(entity);
            reservations.add(reservation);
        }
    }

    public List<Reservation> getReservationsByEntityId(Long cottageId){return reservationRepository.getReservationsByEntityId(cottageId);}

    public List<Reservation> findAllReservationsForEntityId(Long id) {
        return this.reservationRepository.findAllReservationsForEntityId(id);
    }

    public List<Reservation> findAllFastReservationsForEntityid(Long id) {
        return this.reservationRepository.findAllFastReservationsForEntityId(id);
    }


    public List<Reservation> getFastReservationsByBookingEntityId(Long bookingEntityId) {
        List<Reservation> allFastRes = reservationRepository.getFastReservationsByBookingEntityId(bookingEntityId);
        //System.out.println("caocao" + " " + allFastRes.size());
        List<Reservation> activeFastRes = new ArrayList<>();
        for (Reservation r : allFastRes){
            System.out.println(allFastRes.size());
            if ((r.getStartDate()).isAfter(LocalDateTime.now())) activeFastRes.add(r);
        }
        return activeFastRes;
    }

    public List<ReservationDTO> getFastAvailableReservationsDTO(Long bookingEntityId) {
        List<Reservation> allFastRes = reservationRepository.getFastReservationsByBookingEntityId(bookingEntityId);
        //System.out.println("caocao" + " " + allFastRes.size());
        List<ReservationDTO> activeFastRes = new ArrayList<>();
        for (Reservation r : allFastRes){
            System.out.println(allFastRes.size());
            if ((r.getStartDate()).isAfter(LocalDateTime.now()) && r.getClient() == null)
            {
                ReservationDTO reservationDTO = new ReservationDTO(r);
                reservationDTO.setFetchedProperties(r);
                activeFastRes.add(reservationDTO);
            }
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
            Set<AdditionalService> additionalServices = new HashSet<>();
            for (NewAdditionalServiceDTO nas: reservationDTO.getAdditionalServices()) {
                AdditionalService as = additionalServiceRepository.findById(nas.getId()).orElse(null);
                additionalServices.add(as);
            }
            res.setAdditionalServices(additionalServices);
            BookingEntityDTO entityDTO = reservationDTO.getBookingEntity();
            if (entityDTO == null) return null;
            BookingEntity entity = bookingEntityRepository.getEntityById(entityDTO.getId());
            if (entity == null) return null;
            res.setBookingEntity(entity);
            res.setVersion(1);
            //resavanje konfliktne situacije student 2.
            entity.setLocked(true);
            bookingEntityRepository.save(entity);
            res.setVersion(1);
            reservationRepository.save(res);
            entity.setLocked(false);
            bookingEntityRepository.save(entity);

            List<Long> subscribersIds = subscriberRepository.findAllSubscribersForEntityId(res.getBookingEntity().getId());
            List<Client> subscribers = new ArrayList<>();
            for (Long s: subscribersIds){
                Client client = clientRepository.findByIdWithoutReservationsAndWatchedEntities(s);
                if (client != null) subscribers.add(client);
            }
            //if (sendMail(res, subscribers).equals("error")) return null;
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

    @Transactional
    public Reservation addReservation(ReservationDTO reservationDTO) {
        try{
            Reservation res = new Reservation();

            res.setStartDate(reservationDTO.getStartDate());
            res.setCost(reservationDTO.getCost());
            res.setCanceled(false);
            res.setFastReservation(false);
            res.setNumOfDays(reservationDTO.getNumOfDays());
            res.setNumOfPersons(reservationDTO.getNumOfPersons());
            Set<AdditionalService> aServices = new HashSet<>();
            for (NewAdditionalServiceDTO as:reservationDTO.getAdditionalServices()) {
                aServices.add(additionalServiceRepository.findById(as.getId()).orElse(null));
            }
            res.setAdditionalServices(aServices);
            BookingEntityDTO entityDTO = reservationDTO.getBookingEntity();
            if (entityDTO == null) return null;
            BookingEntity entity = bookingEntityRepository.getEntityById(entityDTO.getId());
            if (entity == null) return null;
            res.setBookingEntity(entity);
            res.setVersion(0);
            Client client = clientRepository.findByIdWithoutReservationsAndWatchedEntities(reservationDTO.getClient().getId());


            if(!dateRangeAvailable(reservationDTO.getBookingEntity().getId(),reservationDTO.getStartDate(), reservationDTO.getNumOfDays()))
                return null;

            entity.setLocked(true);
            bookingEntityRepository.save(entity);
            res.setClient(client);
            reservationRepository.save(res);
            entity.setLocked(false);
            bookingEntityRepository.save(entity);

            return res;
        }catch (OptimisticLockException e){
            System.out.println("Conflict seems to have occurred, another user just reserved this entity. Please refresh page and try again\";");
        }
        return null;
    }

    private boolean dateRangeAvailable(Long entityId, LocalDateTime startDate, int numOfDays) {
        return false;

    }


    public Reservation addReservationForClient(ReservationForClientDTO reservationDTO) {
        try{
            Reservation res = new Reservation();

            res.setStartDate(reservationDTO.getStartDate());
            res.setCost(reservationDTO.getCost());
            res.setCanceled(false);
            res.setFastReservation(false);
            res.setNumOfDays(reservationDTO.getNumOfDays());
            res.setNumOfPersons(reservationDTO.getNumOfPersons());
            Set<AdditionalService> aServices = new HashSet<>();
            for (NewAdditionalServiceDTO as:reservationDTO.getAdditionalServices()) {
                aServices.add(additionalServiceRepository.findById(as.getId()).orElse(null));
            }
            res.setAdditionalServices(aServices);
            BookingEntityDTO entityDTO = reservationDTO.getBookingEntity();
            if (entityDTO == null) return null;
            BookingEntity entity = bookingEntityRepository.getEntityById(entityDTO.getId());
            if (entity == null) return null;
            res.setBookingEntity(entity);
            res.setVersion(1);
            String[] tokens = reservationDTO.getClient().split(" ");
            Long clientId = Long.parseLong(tokens[2].substring(1, tokens[2].length()-1));
            Client client = clientRepository.findByIdWithoutReservationsAndWatchedEntities(clientId);

            if (entity.isLocked()){
                System.out.println("Conflict seems to have occurred, another user just reserved this entity. Please refresh page and try again\";");
                return null;
            }
            //resavanje konfliktne situacije student 2.
            entity.setLocked(true);
            bookingEntityRepository.save(entity);
            res.setClient(client);

            entity.setLocked(true);
            bookingEntityRepository.save(entity);
            res.setClient(client);
            reservationRepository.save(res);
            entity.setLocked(false);
            bookingEntityRepository.save(entity);

            //emailService.sendNotificationAboutResToClient(client, res);
            return res;
        }catch (OptimisticLockException e){
            System.out.println("Conflict seems to have occurred, another user just reserved this entity. Please refresh page and try again\";");

        }
        return null;
    }

    public List<ReservationDTO> getReservationsByClientId(Long clientId) {
        List<Reservation> reservations = reservationRepository.getReservationsByClientId(clientId);
        List<ReservationDTO> reservationDTOS = new ArrayList<>();
        for(Reservation res : reservations){
            ReservationDTO newReservation = new ReservationDTO(res);
            newReservation.setBookingEntity(new BookingEntityDTO(res.getBookingEntity()));
            newReservation.setClient(new ClientDTO(res.getClient()));
            reservationDTOS.add(newReservation);
        }
        return reservationDTOS;
    }

    public List<Reservation> getAllActiveOrFutureReservationsForClientId(Long clientId) {
        List<Reservation> reservations = reservationRepository.getReservationsByClientId(clientId);
        List<Reservation> retVal = new ArrayList<>();
        for (Reservation r : reservations) {
            if (!r.isFinished())
                retVal.add(r);
        }
        return retVal;
    }

    public void cancelReservation(Long id) {
        Reservation res = reservationRepository.findById(id).orElse(null);
        if(res != null){
            res.setCanceled(true);
            reservationRepository.save(res);
        }
    }

    public List<String> findAllClientsWithActiveReservations(Long bookingEntityId) {
        List<Reservation> reservations = new ArrayList<>();
        reservations = reservationRepository.findAllRegularAndFastReservationsForEntityIdWithClient(bookingEntityId);
        List<String> clients = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getStartDate().isBefore(LocalDateTime.now()) && r.getEndDate().isAfter(LocalDateTime.now()))
                clients.add(r.getClient().getFirstName() + " " + r.getClient().getLastName() + " (" + r.getClient().getId() + ")");
        }
        return clients;
    }

    public List<Reservation> getAllFinishedReservations() {
        List<Reservation> all = reservationRepository.getAllFinishedReservations();
        List<Reservation> retVal = new ArrayList<Reservation>();
        for (Reservation r : all) {
            if (r.isFinished())
                retVal.add(r);
        }
        return retVal;
    }

    public Reservation reserveFastReservation(ReservationDTO reservationDTO) {
        try{
            Reservation res = reservationRepository.findById(reservationDTO.getId()).orElse(null);
            Client client = clientRepository.findByIdWithoutReservationsAndWatchedEntities(reservationDTO.getClient().getId());
            res.setClient(client);
            reservationRepository.save(res);
            emailService.sendNotificationAboutResToClient(client, res);
            return res;
        }catch (OptimisticLockException e){
            System.out.println("Conflict seems to have occurred, another user reserved this entity before you. Please refresh page and try again\"");
        }
        return null;
    }

    public Reservation findById(long Id) {
        return reservationRepository.findById(Id).orElse(null);
    }

    public void save(Reservation fastReservation) {
        reservationRepository.save(fastReservation);
    }
}
