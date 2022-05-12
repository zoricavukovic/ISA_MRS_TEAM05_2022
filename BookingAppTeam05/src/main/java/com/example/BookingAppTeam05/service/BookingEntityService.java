package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.RatingService;
import com.example.BookingAppTeam05.model.entities.*;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.BookingEntityRepository;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingEntityService {
    private BookingEntityRepository bookingEntityRepository;
    private UserService userService;
    private AdventureService adventureService;
    private CottageService cottageService;
    private ShipService shipService;
    private SearchService searchService;
    private RatingService ratingService;
    private PricelistService pricelistService;
    private ReservationService reservationService;

    @Autowired
    public BookingEntityService(BookingEntityRepository bookingEntityRepository, UserService userService, CottageService cottageService, AdventureService adventureService, ShipService shipService, SearchService searchService, RatingService ratingService, PricelistService pricelistService, ReservationService reservationService) {
        this.bookingEntityRepository = bookingEntityRepository;
        this.userService = userService;
        this.adventureService = adventureService;
        this.cottageService = cottageService;
        this.shipService = shipService;
        this.searchService = searchService;
        this.ratingService = ratingService;
        this.pricelistService = pricelistService;
        this.reservationService = reservationService;
    }

    public List<SearchedBookingEntityDTO> getSearchedBookingEntitiesDTOsByOnwerId(Long id) {
        User owner = userService.findUserById(id);
        List<SearchedBookingEntityDTO> retVal = new ArrayList<>();
        switch (owner.getRole().getName()) {
            case "ROLE_COTTAGE_OWNER": {
                List<Cottage> cottages = cottageService.getCottagesByOwnerId(id);
                cottages.forEach(c -> retVal.add(new SearchedBookingEntityDTO(c)));
            }
            case "ROLE_SHIP_OWNER": {
                List<Ship> ships = shipService.getShipsByOwnerId(id);
                ships.forEach(s -> retVal.add(new SearchedBookingEntityDTO(s)));
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
        s.setAverageRating(avgRating);
        Pricelist pricelist = pricelistService.getCurrentPricelistForEntityId(s.getId());
        s.setEntityPricePerPerson(pricelist.getEntityPricePerPerson());
    }

    public List<SearchedBookingEntityDTO> simpleFilterSearchForBookingEntities(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
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

    public boolean logicalDeleteBookingEntityById(Long id) {
        if (checkExistActiveReservationForEntityId(id))
            return false;
        bookingEntityRepository.logicalDeleteBookingEntityById(id);
        return true;
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

        return entityDTO;
    }
}
