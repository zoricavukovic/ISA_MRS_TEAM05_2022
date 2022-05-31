package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.entities.AdventureDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.dto.entities.ShipDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.service.RatingService;
import com.example.BookingAppTeam05.model.UnavailableDate;
import com.example.BookingAppTeam05.model.entities.*;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.model.repository.entities.AdventureRepository;
import com.example.BookingAppTeam05.model.repository.entities.BookingEntityRepository;
import com.example.BookingAppTeam05.model.repository.entities.CottageRepository;
import com.example.BookingAppTeam05.model.repository.entities.ShipRepository;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingEntityService {
    private BookingEntityRepository bookingEntityRepository;
    private CottageRepository cottageRepository;
    private UserService userService;
    private AdventureService adventureService;
    private CottageService cottageService;
    private ShipService shipService;
    private SearchService searchService;
    private RatingService ratingService;
    private PricelistService pricelistService;
    private ReservationService reservationService;
    private PictureService pictureService;
    private ShipRepository shipRepository;
    private AdventureRepository adventureRepository;

    @Autowired
    public BookingEntityService(BookingEntityRepository bookingEntityRepository, UserService userService,
                                CottageService cottageService, AdventureService adventureService, ShipService shipService,
                                SearchService searchService, RatingService ratingService, PricelistService pricelistService,
                                ReservationService reservationService, PictureService pictureService,
                                CottageRepository cottageRepository, ShipRepository shipRepository,AdventureRepository adventureRepository ) {
        this.bookingEntityRepository = bookingEntityRepository;
        this.userService = userService;
        this.adventureService = adventureService;
        this.cottageService = cottageService;
        this.shipService = shipService;
        this.searchService = searchService;
        this.ratingService = ratingService;
        this.pricelistService = pricelistService;
        this.reservationService = reservationService;
        this.pictureService = pictureService;
        this.cottageRepository = cottageRepository;
        this.shipRepository = shipRepository;
        this.adventureRepository = adventureRepository;
    }

    public List<SearchedBookingEntityDTO> getSearchedBookingEntitiesDTOsByOwnerId(Long id) {
        User owner = userService.findUserById(id);
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
                return null;
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
        return searchService.simpleFilterSearchForBookingEntities(entities, s);
    }

    public SearchedBookingEntityDTO getSearchedBookingEntityDTOByEntityId(Long id) {
        BookingEntity bookingEntity = bookingEntityRepository.getEntityById(id);
        if (bookingEntity == null)
            return null;
        SearchedBookingEntityDTO retVal = new SearchedBookingEntityDTO(bookingEntity);
        setPriceListAndRatingForSearchedBookingEntityDTO(retVal);
        return retVal;
    }

    public BookingEntity getBookingEntityById(Long id) {
        return this.bookingEntityRepository.getEntityById(id);
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

    public BookingEntity getEntityById(Long id) {
        return bookingEntityRepository.getEntityById(id);
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

    public BookingEntityDTO findById(Long id) {
        Optional<EntityType> entityType = bookingEntityRepository.findEntityTypeById(id);
        if (!entityType.isPresent())
            return null;

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
        if(entityDTO.getEntityType() != EntityType.ADVENTURE)
            setAllUnavailableDatesForRange(entityDTO);
        else
            setAllUnavailableDates(entityDTO);

        return entityDTO;
    }

    public List<SearchedBookingEntityDTO> findTopRated(String type) {
        List<BookingEntity> entities = null;
        if(type.equals("cottage"))
            entities = (List<BookingEntity>)(List<?>)cottageRepository.findAll();
        else if(type.equals("ship"))
            entities = (List<BookingEntity>)(List<?>)shipRepository.findAll();
        else
            entities = (List<BookingEntity>)(List<?>)adventureRepository.findAll();
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        for (BookingEntity entity : entities) {
            SearchedBookingEntityDTO s = getSearchedBookingEntityDTOByEntityId(entity.getId());
            retVal.add(s);
        }
        Collections.sort(retVal, (o1, o2) -> {
            if(o1.getAverageRating() > o2.getAverageRating())
                return -1;
            else if(o1.getAverageRating() < o2.getAverageRating())
                return 1;
            else
                return 0;
        });
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
        for(UnavailableDate unavailableDate:entityDTO.getUnavailableDates()){
            int days = 0;
            LocalDateTime date = unavailableDate.getStartTime();
            while(date.isBefore(unavailableDate.getEndTime())){
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
        }
        allUnavailableDates.addAll(additionalDates);
        entityDTO.setAllUnavailableDates(allUnavailableDates);

    }

    public void setNewImagesForBookingEntity(BookingEntity bookingEntity, List<NewImageDTO> images) {
        Set<Picture> pictures = new HashSet<>();

        for (Picture currentPicture : bookingEntity.getPictures()) {
            boolean found = false;
            for (NewImageDTO newImage : images) {
                if (newImage.getImageName().equals(currentPicture.getPicturePath())) {
                    found = true;
                    pictures.add(currentPicture);
                    break;
                }
            }
            if (!found) {
                pictureService.deletePictureByName(currentPicture.getPicturePath());
            }
        }
        for (NewImageDTO newImage : images) {
            boolean found = false;
            for (Picture picture : pictures) {
                if (picture.getPicturePath().equals(newImage.getImageName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                pictureService.tryToSaveNewPictureAndAddToOtherPictures(pictures, newImage);
            }
        }
        bookingEntity.setPictures(pictures);
    }

    public List<SearchedBookingEntityDTO> getSearchedBookingEntities(SearchParamsForEntity searchParams, String type) {
        try {
            List<BookingEntity> entity = null;
            if(type.equals("cottage"))
                entity = (List<BookingEntity>)(List<?>) cottageRepository.findAll();
            else if(type.equals("ship"))
                entity = (List<BookingEntity>)(List<?>) shipRepository.findAll();
            else if(type.equals("instructor"))
                entity = (List<BookingEntity>)(List<?>) adventureRepository.findAll();
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

    private boolean userWithIdOwnsEntityWithId(Long ownerId, Long entityId) {
        User user = this.getOwnerOfEntityId(entityId);
        return user.getId().equals(ownerId);
    }

    @Transactional
    public String tryToLogicalDeleteBookingEntityAndReturnErrorCode(Long entityId, Long userId, String confirmPass) {
        BookingEntity bookingEntity = this.getEntityById(entityId);
        if (bookingEntity == null)
            return "Entity for deleting is not found. Entity id requested: " + entityId;

        User user = userService.findUserById(userId);
        if (!user.getRole().getName().equals("ROLE_ADMIN") && !user.getRole().getName().equals("ROLE_SUPER_ADMIN")) {
            if (!this.userWithIdOwnsEntityWithId(userId, entityId)) {
                return "Invalid delete request. User with id: " + userId + " does not owns entity with id: " + entityId;
            }
        }
        if (user.getRole().getName().equals("ROLE_CLIENT")) {
            return "It seems like you dont have permission to delete this entity. Please try again, or login with another account";
        }

        if (!userService.passwordIsCorrect(user, confirmPass))
            return "Confirmation password is incorrect.";

        if (checkExistActiveReservationForEntityId(entityId))
            return "Entity cant be deleted  cause has reservations.";

        bookingEntityRepository.logicalDeleteBookingEntityById(entityId);
        return null;
    }
}
