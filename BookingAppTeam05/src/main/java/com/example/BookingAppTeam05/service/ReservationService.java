package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.dto.ReservationForClientDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.exception.ConflictException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.NotificationByEmailException;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.EditItemException;
import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.service.entities.AdventureService;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.entities.CottageService;
import com.example.BookingAppTeam05.service.entities.ShipService;
import com.example.BookingAppTeam05.service.users.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private BookingEntityService bookingEntityService;
    private AdditionalServiceService additionalServiceService;
    private CottageService cottageService;
    private ShipService shipService;
    private AdventureService adventureService;
    private ClientService clientService;
    private SubscriberService subscriberService;
    private EmailService emailService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              EmailService emailService, @Lazy BookingEntityService bookingEntityService,
                              AdditionalServiceService additionalServiceService,  @Lazy CottageService cottageService,
                              @Lazy ShipService shipService,  @Lazy AdventureService adventureService,
                              @Lazy ClientService clientService, SubscriberService subscriberService){
        this.reservationRepository = reservationRepository;
        this.bookingEntityService = bookingEntityService;
        this.additionalServiceService = additionalServiceService;
        this.cottageService = cottageService;
        this.shipService = shipService;
        this.adventureService = adventureService;
        this.clientService = clientService;
        this.subscriberService = subscriberService;
        this.emailService = emailService;
    }

    public ReservationService(){}

    public List<Reservation> getReservationsByOwnerId(Long ownerId, String type) {
        List<Reservation> reservations = new ArrayList<>();
        switch (type) {
            case "COTTAGE": {
                List<Cottage> entities = cottageService.getCottagesByOwnerId(ownerId);
                for (BookingEntity entity : entities)
                    addingReservations(reservations, entity);
                break;
            }
            case "SHIP": {
                List<Ship> entities = shipService.getShipsByOwnerId(ownerId);
                for (BookingEntity entity : entities)
                    addingReservations(reservations, entity);
                break;
            }
            case "ADVENTURE": {
                List<Adventure> entities = adventureService.getAdventuresByOwnerId(ownerId);
                for (BookingEntity entity : entities)
                    addingReservations(reservations, entity);
                break;
            }
            default:
                throw new ItemNotFoundException("Reservations not found for this type [" + type + "].");
        }
        return reservations;
    }

    public List<ReservationDTO> getReservationDTOs(Long ownerId, String type) {
        List<Reservation> reservationsFound = getReservationsByOwnerId(ownerId, type);
        return createReservationDTOs(reservationsFound);
    }

    public List<ReservationDTO> getFastReservationDTOs(Long bookingEntityId) {
        List<Reservation> reservationsFound = getFastReservationsByBookingEntityId(bookingEntityId);
        return createReservationDTOs(reservationsFound);
    }

    private List<ReservationDTO> createReservationDTOs(List<Reservation> reservationsFound) {
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation: reservationsFound) {
            ReservationDTO rDTO = new ReservationDTO(reservation);
            rDTO.setBookingEntity(new BookingEntityDTO(reservation.getBookingEntity()));
            if (reservation.getClient() != null)
                rDTO.setClient(new ClientDTO(reservation.getClient()));
            reservationDTOs.add(rDTO);
        }
        return reservationDTOs;
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

    public List<ReservationDTO> filterReservationsByOwnerId(Long ownerId, String type, String name, String time) {
        List<ReservationDTO> reservationDTOs = getReservationDTOs(ownerId, type);
        return filterReservation(reservationDTOs, name, time);
    }

    private void addingReservations(List<Reservation> reservations, BookingEntity entity) {
        List<Reservation> reservationsByCottageId = getReservationsByEntityId(entity.getId());
        for (Reservation reservation : reservationsByCottageId) {
            reservation.setBookingEntity(entity);
            reservations.add(reservation);
        }
    }

    public List<Reservation> getReservationsByEntityId(Long cottageId){return this.reservationRepository.getReservationsByEntityId(cottageId);}

    public List<Reservation> findAllReservationsForEntityId(Long id) {
        return this.reservationRepository.findAllReservationsForEntityId(id);
    }

    public List<Reservation> findAllFastReservationsForEntityId(Long id) {
        return this.reservationRepository.findAllFastReservationsForEntityId(id);
    }

    public List<Reservation> getFastReservationsByBookingEntityId(Long bookingEntityId) {
        List<Reservation> allFastRes = reservationRepository.getFastReservationsByBookingEntityId(bookingEntityId);
        List<Reservation> activeFastRes = new ArrayList<>();
        for (Reservation r : allFastRes){
            if ((r.getStartDate()).isAfter(LocalDateTime.now())) activeFastRes.add(r);
        }
        return activeFastRes;
    }

    public List<ReservationDTO> getFastAvailableReservationsDTO(Long bookingEntityId) {
        List<Reservation> allFastRes = reservationRepository.getFastReservationsByBookingEntityId(bookingEntityId);
        List<ReservationDTO> activeFastRes = new ArrayList<>();
        for (Reservation r : allFastRes){
            if ((r.getStartDate()).isAfter(LocalDateTime.now()) && r.getClient() == null)
            {
                ReservationDTO reservationDTO = new ReservationDTO(r);
                reservationDTO.setFetchedProperties(r);
                activeFastRes.add(reservationDTO);
            }
        }
        return activeFastRes;
    }

    public Reservation findById(long Id) {
        return reservationRepository.findFastReservationsById(Id).orElse(null);
    }

    public void save(Reservation fastReservation) {
        reservationRepository.save(fastReservation);
    }

    @Transactional
    public Reservation addFastReservation(ReservationDTO reservationDTO){
        try{
            Reservation res = new Reservation();
            if (reservationDTO.getStartDate().isBefore(LocalDateTime.now()))
                throw new CreateItemException("Can't create action with start date before today.");
            res.setStartDate(reservationDTO.getStartDate());
            res.setCost(reservationDTO.getCost());
            res.setCanceled(false);
            res.setFastReservation(true);
            res.setNumOfDays(reservationDTO.getNumOfDays());
            res.setNumOfPersons(reservationDTO.getNumOfPersons());
            Set<AdditionalService> additionalServices = new HashSet<>();
            for (NewAdditionalServiceDTO nas: reservationDTO.getAdditionalServices()) {
                AdditionalService as = additionalServiceService.findAdditionalServiceById(nas.getId());
                if (as == null) throw new ItemNotFoundException("Can't find additional service with name: " + nas.getServiceName());
                additionalServices.add(as);
            }
            res.setAdditionalServices(additionalServices);
            BookingEntityDTO entityDTO = reservationDTO.getBookingEntity();
            if (entityDTO == null)  throw new CreateItemException("Can't create action without set entity for reservation.");
            BookingEntity entity = bookingEntityService.getBookingEntityById(entityDTO.getId());
            if (entity == null) throw new ItemNotFoundException("Can't find entity for reservation.");
            res.setBookingEntity(entity);
            res.setVersion(1);
            //resavanje konfliktne situacije student 2.
            entity.setLocked(true);
            bookingEntityService.save(entity);
            res.setVersion(1);
            reservationRepository.save(res);
            entity.setLocked(false);
            bookingEntityService.save(entity);

            List<Long> subscribersIds = subscriberService.findAllSubscribersForEntityId(res.getBookingEntity().getId());
            List<Client> subscribers = new ArrayList<>();
            for (Long s: subscribersIds){
                Client client = clientService.findClientByIdWithoutReservationsAndWatchedEntities(s);
                if (client != null) subscribers.add(client);
            }
            //sendMail(res, subscribers);
            return res;
        }catch (ObjectOptimisticLockingFailureException e){
            throw new ConflictException("Conflict seems to have occurred. This entity has reservation in same period. Please refresh page and try again");
        }
    }

    private void sendMail(Reservation reservation, List<Client> subscribers){
        try {
            emailService.sendNotificaitionAsync(reservation, subscribers);
        } catch(  MailException | InterruptedException e ){
            throw new NotificationByEmailException("Error happened during sending email.");
        }
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

            for (NewAdditionalServiceDTO nas:reservationDTO.getAdditionalServices()) {
                AdditionalService as = additionalServiceService.findAdditionalServiceById(nas.getId());
                if (as == null) throw new ItemNotFoundException("Can't find additional service with name: " + nas.getServiceName());
                aServices.add(as);
            }
            res.setAdditionalServices(aServices);
            BookingEntityDTO entityDTO = reservationDTO.getBookingEntity();
            if (entityDTO == null) throw new CreateItemException("Can't create reservation without set entity for reservation.");
            BookingEntity entity = bookingEntityService.getBookingEntityById(entityDTO.getId());
            if (entity == null) throw new ItemNotFoundException("Can't find entity for reservation.");
            res.setBookingEntity(entity);
            res.setVersion(0);
            if (reservationDTO.getClient() == null) throw new CreateItemException("Can't reserve reservation without set client.");
            Client client = clientService.findClientByIdWithoutReservationsAndWatchedEntities(reservationDTO.getClient().getId());
            if (client == null) throw new ItemNotFoundException("Can't find client who want to reserve reservation.");
            if(!dateRangeAvailable(reservationDTO.getBookingEntity().getId(),reservationDTO.getStartDate(), reservationDTO.getNumOfDays()))
                throw new CreateItemException("Can't create reservation cause date range is unavailable. Refresh page and try again!");

            if(clientCanceledInPeriod(reservationDTO.getClient().getId(), reservationDTO.getBookingEntity().getId(),reservationDTO.getStartDate(), reservationDTO.getNumOfDays()))
                throw new CreateItemException("Can't create reservation cause you already canceled this booking entity in this period.");


            entity.setLocked(true);
            bookingEntityService.save(entity);
            res.setClient(client);
            reservationRepository.save(res);
            entity.setLocked(false);
            bookingEntityService.save(entity);
            return res;
        }catch (ObjectOptimisticLockingFailureException e){
            throw new ConflictException("Conflict seems to have occurred. Another user just reserved this entity. Please refresh page and try again");
        }
    }

    private boolean clientCanceledInPeriod(Long clientId, Long entityId, LocalDateTime startDate, int numOfDays) {
        LocalDateTime endDate = startDate.plusDays(numOfDays);
        return bookingEntityService.checkIfClientCanceledReservation(clientId, entityId, startDate, endDate);
    }

    private boolean dateRangeAvailable(Long entityId, LocalDateTime startDate, int numOfDays) {
        LocalDateTime endDate = startDate.plusDays(numOfDays);
        return !bookingEntityService.checkExistReservationInPeriodForEntityId(entityId, startDate, endDate);
    }

    @Transactional
    public String addReservationForClient(ReservationForClientDTO reservationDTO) {
        try{
            Reservation res = new Reservation();
            res.setStartDate(reservationDTO.getStartDate());
            res.setCost(reservationDTO.getCost());
            res.setCanceled(false);
            res.setFastReservation(false);
            res.setNumOfDays(reservationDTO.getNumOfDays());
            res.setNumOfPersons(reservationDTO.getNumOfPersons());
            Set<AdditionalService> aServices = new HashSet<>();

            for (NewAdditionalServiceDTO nas:reservationDTO.getAdditionalServices()) {
                AdditionalService as = additionalServiceService.findAdditionalServiceById(nas.getId());
                if (as == null) throw new ItemNotFoundException("Can't find additional service with name: " + nas.getServiceName());
                aServices.add(as);
            }
            res.setAdditionalServices(aServices);
            BookingEntityDTO entityDTO = reservationDTO.getBookingEntity();
            if (entityDTO == null) throw new CreateItemException("Can't create reservation for client without set entity for reservation.");
            BookingEntity entity = bookingEntityService.getEntityById(entityDTO.getId());
            if (entity == null) throw new ItemNotFoundException("Can't find entity for reservation.");
            res.setBookingEntity(entity);
            res.setVersion(1);
            if (reservationDTO.getClient() == null) throw new CreateItemException("Can't reserve reservation for client without set client.");
            String[] tokens = reservationDTO.getClient().split(" ");
            Long clientId = Long.parseLong(tokens[2].substring(1, tokens[2].length()-1));
            Client client = clientService.findClientByIdWithoutReservationsAndWatchedEntities(clientId);
            if (client == null) throw new ItemNotFoundException("Can't find client for reservation.");

            if(!dateRangeAvailable(reservationDTO.getBookingEntity().getId(),reservationDTO.getStartDate(), reservationDTO.getNumOfDays()))
                throw new CreateItemException("Can't create reservation cause date range is already unavailable. Refresh page and try again!");

            if(clientCanceledInPeriod(clientId, reservationDTO.getBookingEntity().getId(),reservationDTO.getStartDate(), reservationDTO.getNumOfDays()))
                throw new CreateItemException("Can't create reservation cause you already canceled this booking entity in this period.");

            //resavanje konfliktne situacije student 2.
            entity.setLocked(true);
            bookingEntityService.save(entity);
            res.setClient(client);
            reservationRepository.save(res);
            entity.setLocked(false);
            bookingEntityService.save(entity);

            //emailService.sendNotificationAboutResToClient(client, res);
            return res.getId().toString();
        }catch (ObjectOptimisticLockingFailureException e){
            throw new ConflictException("Conflict seems to have occurred, another user just reserved this entity. Please refresh page and try again");
        }
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
        List<Reservation> reservations = reservationRepository.findAllRegularAndFastReservationsForEntityIdWithClient(bookingEntityId);
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

    @Transactional
    public Reservation reserveFastReservation(ReservationDTO reservationDTO) {
        try{
            Reservation res = reservationRepository.findFastReservationsById(reservationDTO.getId()).orElse(null);
            if (res == null) throw new ItemNotFoundException("Can't find fast reservation entity for reservation.");
            if (reservationDTO.getClient() == null) throw new EditItemException("Can't reserve fast reservation without set client.");
            Client client = clientService.findClientByIdWithoutReservationsAndWatchedEntities(reservationDTO.getClient().getId());
            if (client == null) throw new ItemNotFoundException("Can't find client who want to reserve fast reservation.");
            res.setClient(client);

            if(!dateRangeAvailable(reservationDTO.getBookingEntity().getId(),reservationDTO.getStartDate(), reservationDTO.getNumOfDays()))
                throw new CreateItemException("Can't create reservation cause date range is already unavailable. Refresh page and try again!");

            if(clientCanceledInPeriod(reservationDTO.getClient().getId(), reservationDTO.getBookingEntity().getId(),reservationDTO.getStartDate(), reservationDTO.getNumOfDays()))
                throw new CreateItemException("Can't create reservation cause you already canceled this booking entity in this period.");

            reservationRepository.save(res);
            //emailService.sendNotificationAboutResToClient(client, res);
            return res;
        }catch (ObjectOptimisticLockingFailureException e){
            throw new ConflictException("Conflict seems to have occurred, another user reserved this entity in before you create this action in same period. Please refresh page and try again");
        }
    }

    public List<Reservation> findAllCanceledReservationsForEntityid(Long entityId) {
        return reservationRepository.findAllCanceledReservationsForEntityId(entityId);
    }
}
