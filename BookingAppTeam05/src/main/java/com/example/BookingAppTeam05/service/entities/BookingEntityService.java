package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.calendar.UnavailableDateDTO;
import com.example.BookingAppTeam05.dto.entities.AdventureDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.dto.entities.ShipDTO;
import com.example.BookingAppTeam05.exception.BookingAppException;
import com.example.BookingAppTeam05.exception.ConflictException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.UnauthorisedException;
import com.example.BookingAppTeam05.exception.database.EditItemException;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.service.RatingService;
import com.example.BookingAppTeam05.model.entities.*;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.entities.BookingEntityRepository;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.ClientService;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingEntityService {
    private BookingEntityRepository bookingEntityRepository;
    private UserService userService;
    private ClientService clientService;
    private AdventureService adventureService;
    private CottageService cottageService;
    private ShipService shipService;
    private SearchService searchService;
    private RatingService ratingService;
    private PricelistService pricelistService;
    private ReservationService reservationService;

    @Autowired
    public BookingEntityService(BookingEntityRepository bookingEntityRepository, UserService userService,
                                ClientService clientService, CottageService cottageService, AdventureService adventureService, ShipService shipService,
                                SearchService searchService, RatingService ratingService, PricelistService pricelistService,
                                ReservationService reservationService) {
        this.bookingEntityRepository = bookingEntityRepository;
        this.userService = userService;
        this.clientService = clientService;
        this.adventureService = adventureService;
        this.cottageService = cottageService;
        this.shipService = shipService;
        this.searchService = searchService;
        this.ratingService = ratingService;
        this.pricelistService = pricelistService;
        this.reservationService = reservationService;
    }

    public BookingEntityService(){}

    public BookingEntity findBookingEntityById(Long id){
        return bookingEntityRepository.findById(id).orElse(null);
    }

    public List<SearchedBookingEntityDTO> getSearchedBookingEntitiesDTOsByOwnerId(Long id) {
        User owner = userService.findUserById(id);
        if (owner == null)
            throw new ItemNotFoundException("Can't find user with id: " + id);
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        switch (owner.getRole().getName()) {
            case "ROLE_COTTAGE_OWNER": {
                List<Cottage> cottages = cottageService.getCottagesByOwnerId(id);
                cottages.forEach(c -> retVal.add(new SearchedBookingEntityDTO(c)));
                break;
            }
            case "ROLE_SHIP_OWNER": {
                List<Ship> ships = shipService.getShipsByOwnerId(id);
                ships.forEach(s -> retVal.add(new SearchedBookingEntityDTO(s)));
                break;
            }
            case "ROLE_INSTRUCTOR": {
                List<Adventure> adventures = adventureService.getAdventuresByOwnerId(id);
                adventures.forEach(a -> retVal.add(new SearchedBookingEntityDTO(a)));
                break;
            }
            default: {
                throw new UnauthorisedException("Unauthorised action for user with id " + id);
            }
        }
        for (SearchedBookingEntityDTO s : retVal) {
            setPriceListAndRatingForSearchedBookingEntityDTO(s);
        }
        return retVal;
    }

    private void setPriceListAndRatingForSearchedBookingEntityDTO(SearchedBookingEntityDTO s) {
        Float avgRating = ratingService.getAverageRatingForEntityId(s.getId());
        if (avgRating == null) s.setAverageRating((float) 0);
        else s.setAverageRating(avgRating);
        Pricelist pricelist = pricelistService.getCurrentPricelistForEntityId(s.getId());
        s.setEntityPricePerPerson(pricelist.getEntityPricePerPerson());
    }

    public List<SearchedBookingEntityDTO> simpleFilterSearchForBookingEntities(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityOwnerDTO s) {
        try {
            return searchService.simpleFilterSearchForBookingEntities(entities, s);
        } catch (BookingAppException be){
            throw new BookingAppException("Something happen with server. Try again later!");
        }
    }

    public SearchedBookingEntityDTO getSearchedBookingEntityDTOByEntityId(Long id) {
        BookingEntity bookingEntity = bookingEntityRepository.getEntityById(id);
        if (bookingEntity == null)
            throw new ItemNotFoundException("Can't find entity with id: " + id + ". Refresh page and try again!");
        SearchedBookingEntityDTO retVal = new SearchedBookingEntityDTO(bookingEntity);
        setPriceListAndRatingForSearchedBookingEntityDTO(retVal);
        return retVal;
    }

    public BookingEntity getBookingEntityWithUnavailableDatesById(Long id) {
        return this.bookingEntityRepository.getEntityWithUnavailableDatesById(id);
    }

    public boolean checkExistActiveReservationForEntityId(Long id) {
        return reservationService.findAllActiveReservationsForEntityid(id).size() != 0;
    }

    public boolean checkExistReservationInPeriodForEntityId(Long id, LocalDateTime start, LocalDateTime end) {
        List<Reservation> reservations = reservationService.findAllActiveReservationsForEntityid(id);
        for (LocalDateTime date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            for (Reservation r : reservations) {
                if (date.isAfter(r.getStartDate()) && date.isBefore(r.getEndDate()))
                    return true;
                if (date.equals(r.getStartDate()) || date.equals(r.getEndDate()))
                    return true;
            }
        }
        return false;
    }

    public boolean checkIfClientCanceledReservation(Long clientId, Long entityId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Reservation> reservations = reservationService.findAllCanceledReservationsForEntityid(entityId);
        for (LocalDateTime date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            for (Reservation r : reservations) {
                if (date.isAfter(r.getStartDate()) && date.isBefore(r.getEndDate()) && r.getClient().getId() == clientId)
                    return true;
                if ((date.equals(r.getStartDate()) || date.equals(r.getEndDate())) && r.getClient().getId() == clientId)
                    return true;
            }
        }
        return false;
    }

    public BookingEntity getEntityById(Long id) {
        BookingEntity retVal = bookingEntityRepository.getEntityById(id);
        if (retVal == null)
            throw new ItemNotFoundException("Entity is not found. Refresh page and try again!");
        return retVal;
    }

    public BookingEntity getBookingEntityById(Long id) {
        return getEntityById(id);
    }

    public User getOwnerOfEntityId(Long entityId) {
        BookingEntity bookingEntity = getEntityById(entityId);
        switch (bookingEntity.getEntityType()) {
            case COTTAGE: return cottageService.getCottageOwnerOfCottageId(bookingEntity.getId());
            case ADVENTURE: return adventureService.getInstructorOfAdventureId(bookingEntity.getId());
            case SHIP: return shipService.getShipOwnerOfShipId(bookingEntity.getId());
            default: return null;
        }
    }

    @Transactional
    public BookingEntityDTO findById(Long id) {
        Optional<EntityType> entityType = bookingEntityRepository.findEntityTypeById(id);
        if (!entityType.isPresent())
            throw new ItemNotFoundException("Can't find entity with id " + id + ". Refresh page and try again!");

        BookingEntityDTO entityDTO = null;
        switch (entityType.get().name()){
            case "COTTAGE":{
                Cottage cottage = cottageService.findById(id);
                CottageDTO cottageDTO = null;
                if (cottage != null){
                    cottageDTO = new CottageDTO(cottage);
                    cottageDTO.setFetchedProperties(cottage);
                    entityDTO = cottageDTO;
                }
                break;
            }
            case "SHIP":{
                Ship ship = shipService.findById(id);
                ShipDTO shipDTO;
                if (ship != null){
                    shipDTO = new ShipDTO(ship);
                    shipDTO.setFetchedProperties(ship);
                    entityDTO = shipDTO;
                }
                break;
            }

            case "ADVENTURE":
            {
                Adventure adventure = adventureService.findById(id);
                AdventureDTO adventureDTO;
                if (adventure != null) {
                    adventureDTO = new AdventureDTO(adventure);
                    adventureDTO.setFetchedProperties(adventure);
                    entityDTO = adventureDTO;
                }
                break;
            }
            default:
                break;
        }
        if (entityDTO == null)
            throw new ItemNotFoundException("Can't find entity with id " + id + ". Refresh page and try again!");

        if(entityDTO.getEntityType() != EntityType.ADVENTURE)
            setAllUnavailableDatesForRange(entityDTO);
        else
            setAllUnavailableDates(entityDTO);

        return entityDTO;
    }

    public List<SearchedBookingEntityDTO> findTopRated(String type) {
        List<BookingEntity> entities;
        if(type.equals("cottage"))
            entities = (List<BookingEntity>)(List<?>)cottageService.findAll();
        else if(type.equals("ship"))
            entities = (List<BookingEntity>)(List<?>)shipService.findAll();
        else
            entities = (List<BookingEntity>)(List<?>)adventureService.findAll();
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (BookingEntity entity : entities) {
            SearchedBookingEntityDTO s = getSearchedBookingEntityDTOByEntityId(entity.getId());
            retVal.add(s);
        }
        retVal.sort((o1, o2) -> o2.getAverageRating().compareTo(o1.getAverageRating()));
        retVal = retVal.stream().limit(3).collect(Collectors.toList());
        return retVal;
    }

    private void setAllUnavailableDates(BookingEntityDTO entityDTO){
        Set<LocalDateTime> unavailableDates = new HashSet<>();
        List<ReservationDTO> NotCanceledReservations = entityDTO.getReservations().stream().filter(e-> !e.isCanceled()).collect(Collectors.toList());
        for(ReservationDTO reservation:NotCanceledReservations){
            LocalDateTime onlyDate = reservation.getStartDate().withHour(0).withMinute(0).withSecond(0).withNano(0);
            Set<Integer> unavailableHours = new HashSet<>();
            for(ReservationDTO reservationCheck:NotCanceledReservations){
                LocalDateTime onlyDate2 = reservationCheck.getStartDate().withHour(0).withMinute(0).withSecond(0).withNano(0);
                if(onlyDate.isEqual(onlyDate2))
                    unavailableHours.add(reservationCheck.getStartDate().getHour());
            }
            if(unavailableHours.size() == 4)
                unavailableDates.add(reservation.getStartDate());
        }

        entityDTO.setAllUnavailableDates(unavailableDates);

    }

    private void setAllUnavailableDatesForRange(BookingEntityDTO entityDTO){
        Set<LocalDateTime> allUnavailableDates = new HashSet<>();
        Set<LocalDateTime> lastDates = new HashSet<>();
        List<ReservationDTO> NotCanceledReservations = entityDTO.getReservations().stream().filter(e-> !e.isCanceled()).collect(Collectors.toList());
        for(ReservationDTO reservation:NotCanceledReservations){
            for(int i=0;i< reservation.getNumOfDays();i++)
                allUnavailableDates.add(reservation.getStartDate().plusDays(i));
            if(reservation.getStartDate().getHour() >= 21)
                allUnavailableDates.add(reservation.getStartDate().plusDays(reservation.getNumOfDays()));
            else
                lastDates.add(reservation.getStartDate().plusDays(reservation.getNumOfDays()));
        }
        for(UnavailableDateDTO unavailableDate:entityDTO.getUnavailableDates()){
            int days = 0;
            LocalDateTime date = unavailableDate.getStartDate();
            while(date.isBefore(unavailableDate.getEndDate())){
                allUnavailableDates.add(date);
                days++;
                date = date.plusDays(days);
            }
        }
        Set<LocalDateTime> additionalDates = new HashSet<>();

        for(LocalDateTime unDate: allUnavailableDates){
            for(LocalDateTime lastDay: lastDates){
                long hours = Math.abs(ChronoUnit.HOURS.between(unDate, lastDay));
                if(hours < 24)
                    additionalDates.add(lastDay);
            }
            if(unDate.getHour() < 9){
                LocalDateTime newDate = unDate.minusDays(1);
                additionalDates.add(newDate);
            }
        }
        allUnavailableDates.addAll(additionalDates);
        entityDTO.setAllUnavailableDates(allUnavailableDates);

    }

    public List<SearchedBookingEntityDTO> getSearchedBookingEntities(SearchParamsForEntity searchParams, String type) {
        try {
            List<BookingEntity> entity = null;
            switch (type) {
                case "cottage":
                    entity = (List<BookingEntity>) (List<?>) cottageService.findAll();
                    break;
                case "ship":
                    entity = (List<BookingEntity>) (List<?>) shipService.findAll();
                    break;
                case "instructor":
                    entity = (List<BookingEntity>) (List<?>) adventureService.findAll();
                    break;
            }
            if (entity == null)
                return null;

            List<BookingEntity> searchedEntities = searchService.searchBookingEntities(entity, searchParams);
            List<SearchedBookingEntityDTO> resultDTO = new ArrayList<>();
            for (BookingEntity ent: searchedEntities){
                SearchedBookingEntityDTO searchedBookingEntityDTO = getSearchedBookingEntityDTOByEntityId(ent.getId());
                resultDTO.add(searchedBookingEntityDTO);
            }
            return resultDTO;
        } catch (Exception e) {
            return null;
        }
    }

    public List<SearchedBookingEntityDTO> getSubscribedEntitiesForClient(Long clientId) {
        List<Long> subsEntitiesIds = clientService.findSubscribedEntitiesByClientId(clientId);
        List<SearchedBookingEntityDTO> retVal = convertToSearchBookingEntitiesDTOList(subsEntitiesIds);
        if (retVal == null)
            throw new ItemNotFoundException("Can't load subscribed entities for client. Try again!");
        return retVal;
    }

    public List<SearchedBookingEntityDTO> convertToSearchBookingEntitiesDTOList(List<Long> entitiesIds){
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (Long entityId: entitiesIds) {
            SearchedBookingEntityDTO item = getSearchedBookingEntityDTOByEntityId(entityId);
            if(item != null)
                retVal.add(getSearchedBookingEntityDTOByEntityId(entityId));
        }
        return retVal;
    }

    public List<SearchedBookingEntityDTO> subscribeClientWithEntity(Long clientId, Long entityId) {
        Client client = clientService.findById(clientId);
        BookingEntity bookingEntity = bookingEntityRepository.findByIdWithoutParams(entityId).orElse(null);
        if (bookingEntity == null)
            throw new ItemNotFoundException("Can't find entity with id " + entityId + ". Refresh page and try again!");
        Set<BookingEntity> watched = client.getWatchedEntities();
        if(watched.stream().filter(entity -> Objects.equals(entity.getId(), bookingEntity.getId())).collect(Collectors.toList()).size() == 0) {
            watched.add(bookingEntity);
            //client.setWatchedEntities(watched);
            clientService.save(client);
        }
        Set<Client> clients = bookingEntity.getSubscribedClients();
        clients.add(client);
        bookingEntity.setSubscribedClients(clients);
        bookingEntityRepository.save(bookingEntity);
        List<Long> entityIds = new ArrayList<>();
        for (BookingEntity entity : watched) {
            entityIds.add(entity.getId());
        }
        return convertToSearchBookingEntitiesDTOList(entityIds);
    }

    public List<SearchedBookingEntityDTO> unsubscribeClientWithEntity(Long clientId, Long entityId) {
        Client client = clientService.findById(clientId);
        BookingEntity bookingEntity = bookingEntityRepository.findById(entityId).orElse(null);
        if (bookingEntity == null)
            throw new ItemNotFoundException("Can't find entity with id " + entityId + ". Refresh page and try again!");
        Set<Client> clients = bookingEntity.getSubscribedClients();
        clients.removeIf(client1 -> Objects.equals(client1.getId(), clientId));
        bookingEntity.setSubscribedClients(clients);
        Set<BookingEntity> watched = client.getWatchedEntities();
        watched.removeIf(entity -> Objects.equals(entity.getId(), entityId));
        client.setWatchedEntities(watched);
        clientService.save(client);
        bookingEntityRepository.save(bookingEntity);
        List<Long> entityIds = new ArrayList<>();
        for (BookingEntity entity : watched) {
            entityIds.add(entity.getId());
        }
        return convertToSearchBookingEntitiesDTOList(entityIds);
    }

    private boolean userWithIdOwnsEntityWithId(Long ownerId, Long entityId) {
        User user = this.getOwnerOfEntityId(entityId);
        if (user == null)
            throw new ItemNotFoundException("Can't find user data in system. Try again!");
        return user.getId().equals(ownerId);
    }

    @Transactional
    public void tryToLogicalDeleteBookingEntityAndReturnErrorCode(Long entityId, Long userId, String confirmPass) {
        try {
            BookingEntity bookingEntity = this.getEntityById(entityId);
            if (bookingEntity == null)
                throw new ItemNotFoundException("Entity for deleting is not found. Entity id requested: " + entityId);

            User user = userService.findUserById(userId);
            if (user == null)
                throw new ItemNotFoundException("Can't find your data in system. Try again!");

            if (!user.getRole().getName().equals("ROLE_ADMIN") && !user.getRole().getName().equals("ROLE_SUPER_ADMIN")) {
                if (!this.userWithIdOwnsEntityWithId(userId, entityId))
                    throw new EditItemException("Invalid delete request. User with id: " + userId + " does not owns entity with id: " + entityId);
            }

            if (user.getRole().getName().equals("ROLE_CLIENT"))
                throw new UnauthorisedException("It seems like you dont have permission to delete this entity. Please try again, or login with another account");

            if (!userService.passwordIsCorrect(user, confirmPass))
                throw new EditItemException("Confirmation password is incorrect.");

            if (checkExistActiveReservationForEntityId(entityId))
                throw new EditItemException("Entity can't be deleted cause has reservations.");

            //resavanje konfliktne situacije student 2.
            bookingEntity.setLocked(true);
            bookingEntityRepository.save(bookingEntity);
            bookingEntity.setDeleted(true);
            bookingEntity.setLocked(false);
            bookingEntityRepository.save(bookingEntity);
        } catch (ObjectOptimisticLockingFailureException e){
            throw new ConflictException("Conflict seems to have occurred. Can't delete entity cause another user just reserved this entity. Please refresh page and try again");
        }
    }

    public void save(BookingEntity bookingEntity) {
        bookingEntityRepository.save(bookingEntity);
    }

    public void checkIfCanEdit(Long entityId) {
        getEntityById(entityId);
        if (checkExistActiveReservationForEntityId(entityId)){
            throw new ItemNotFoundException("Can't edit entity cause has reservations.");
        }
    }

    public List<BookingEntityDTO> getBookingEntitiesDTOsForOwnerId(Long id) {
        List<BookingEntity> bookingEntities = userService.getBookingEntitiesByOwnerId(id);
        List<BookingEntityDTO> retVal = new ArrayList<>();
        for (BookingEntity b : bookingEntities) {
            retVal.add(new BookingEntityDTO(b));
        }
        return retVal;
    }


}
