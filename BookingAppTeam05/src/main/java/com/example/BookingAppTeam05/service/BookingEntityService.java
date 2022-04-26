package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.SimpleSearchForBookingEntityDTO;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.RatingService;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.BookingEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    @Autowired
    public BookingEntityService(BookingEntityRepository bookingEntityRepository, UserService userService, CottageService cottageService, AdventureService adventureService, ShipService shipService, SearchService searchService, RatingService ratingService, PricelistService pricelistService) {
        this.bookingEntityRepository = bookingEntityRepository;
        this.userService = userService;
        this.adventureService = adventureService;
        this.cottageService = cottageService;
        this.shipService = shipService;
        this.searchService = searchService;
        this.ratingService = ratingService;
        this.pricelistService = pricelistService;
    }

    public List<SearchedBookingEntityDTO> getSearchedBookingEntitiesDTOsByOnwerId(Long id) {
        User owner = userService.getUserById(id);
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
            Float avgRating = ratingService.getAverageRatingForEntityId(s.getId());
            s.setAverageRating(avgRating);
            Pricelist pricelist = pricelistService.getCurrentPricelistForEntityId(s.getId());
            s.setEntityPricePerPerson(pricelist.getEntityPricePerPerson());
        }
        return retVal;
    }

    public List<SearchedBookingEntityDTO> simpleFilterSearchForBookingEntities(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
        return searchService.simpleFilterSearchForBookingEntities(entities, s);
    }
}
