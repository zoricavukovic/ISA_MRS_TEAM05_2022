package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.SearchParamsForEntity;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.SimpleSearchForBookingEntityOwnerDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.UnavailableDate;
import com.example.BookingAppTeam05.model.entities.*;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {


    public SearchService( ) {

    }

    private List<SearchedBookingEntityDTO> searchByName(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityOwnerDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        String sName = s.getName().trim().toLowerCase();
        if (!sName.equals(""))
            result = result.stream().filter(i -> i.getName().toLowerCase().contains(sName)).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByAddress(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityOwnerDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        String sAddress = s.getAddress().trim().toLowerCase();
        if (!sAddress.equals(""))
            result = result.stream().filter(i -> i.getAddress().toLowerCase().contains(sAddress)).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByPlaceId(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityOwnerDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        Long placeId = s.getPlaceId();
        if (placeId != null)
            result = result.stream().filter(i -> i.getPlace().getId().equals(placeId)).collect(Collectors.toList());
        return result;
    }
    private List<BookingEntity> searchByPlaceId(List<BookingEntity> entities, Long placeId) {
        List<BookingEntity> result = entities;
        if (placeId != null)
            result = result.stream().filter(i -> i.getPlace().getId().equals(placeId)).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByRating(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityOwnerDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        Float minRating = s.getMinRating();
        if (minRating != null)
            result = result.stream().filter(i -> i.getAverageRating()!=null &&  i.getAverageRating() >= minRating).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByMinAndMaxPrice(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityOwnerDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        Double minCost = s.getMinCostPerPerson();
        Double maxCost = s.getMaxCostPerPerson();

        if (minCost != null && maxCost != null) {
            result = result.stream().filter(i -> (i.getEntityPricePerPerson() >= minCost && i.getEntityPricePerPerson() <= maxCost)).collect(Collectors.toList());
        } else if (minCost != null) {
            result = result.stream().filter(i -> i.getEntityPricePerPerson() >= minCost).collect(Collectors.toList());
        } else if (maxCost != null) {
            result = result.stream().filter(i -> i.getEntityPricePerPerson() <= maxCost).collect(Collectors.toList());
        }
        return result;
    }

    public List<SearchedBookingEntityDTO> simpleFilterSearchForBookingEntities(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityOwnerDTO s) {
        List<SearchedBookingEntityDTO> result;
        result = searchByName(entities, s);
        if (result.size() == 0) return result;

        result = searchByAddress(result, s);
        if (result.size() == 0) return result;

        result = searchByPlaceId(result, s);
        if (result.size() == 0) return result;

        result = searchByRating(result, s);
        if (result.size() == 0) return result;

        result = searchByMinAndMaxPrice(result, s);
        return result;
    }


    public List<BookingEntity> searchBookingEntities(List<BookingEntity> entities, SearchParamsForEntity searchParams) {
        List<BookingEntity> result;
        result = searchByPlaceId(entities, searchParams.getPlaceId());
        if(entities.get(0).getEntityType() != EntityType.ADVENTURE)
            result = searchByDateRange(result, searchParams.getStartDate(), searchParams.getEndDate());
        else
            result = searchByDate(result, searchParams.getStartDate());
        if(entities.get(0).getEntityType() != EntityType.COTTAGE)
            result = searchByMaxPersons(result, searchParams.getNumOfPersons());
        return result;
    }

    private List<BookingEntity> searchByDate(List<BookingEntity> entities, LocalDateTime startDate) {
        List<BookingEntity> results = new ArrayList<>();
        results.addAll(entities);
        if (startDate != null) {
            startDate = startDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            for (BookingEntity entity : entities) {
                int takenTimeInDate = 0;
                boolean unavailable = false;
                for (Reservation reservation : entity.getReservations()) {
                    LocalDateTime onlyStartDate = reservation.getStartDate().withHour(0).withMinute(0).withSecond(0).withNano(0);
                    if (onlyStartDate.isEqual(startDate))
                        takenTimeInDate++;
                }
                for(UnavailableDate unavailableDate:entity.getUnavailableDates()){
                    if(isUnavailableDate(startDate, unavailableDate))
                        unavailable = true;
                }
                if(takenTimeInDate == 4 || unavailable)
                    results.remove(entity);
            }

        }
        return results;
    }

    private boolean isUnavailableDate(LocalDateTime startDate, UnavailableDate unavailableDate) {
        LocalDateTime onlyStartDate = unavailableDate.getStartTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime onlyEndDate = unavailableDate.getEndTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return (onlyStartDate.isBefore(startDate) && onlyEndDate.isAfter(startDate)) ||
                (onlyStartDate.isEqual(startDate) && unavailableDate.getStartTime().getHour() < 12) ||
                (onlyEndDate.isEqual(startDate) && unavailableDate.getEndTime().getHour() > 17);
    }

    private List<BookingEntity> searchByMaxPersons(List<BookingEntity> entities, Long numOfPersons) {
        List<BookingEntity> result = entities;
        if (numOfPersons != null)
            result = result.stream().filter(i -> {
                if(i.getEntityType() == EntityType.SHIP)
                    return ((Ship)i).getMaxNumOfPersons() >= numOfPersons;
                return ((Adventure)i).getMaxNumOfPersons() >= numOfPersons;
            }).collect(Collectors.toList());
        return result;
    }

    private List<BookingEntity> searchByDateRange(List<BookingEntity> entities, LocalDateTime startDate, LocalDateTime endDate) {
        List<BookingEntity> results = new ArrayList<>();
        results.addAll(entities);
        if (startDate != null && endDate != null) {
            startDate = startDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            endDate = endDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            for (BookingEntity entity : entities) {
                for (Reservation reservation : entity.getReservations()) {
                    if (isDateRangeTaken(startDate, endDate, reservation.getStartDate(), reservation.getEndDate())) {
                        results.remove(entity);
                        break;
                    }
                }
                for(UnavailableDate unavailableDate:entity.getUnavailableDates()){
                    if(isDateRangeTaken(startDate,endDate, unavailableDate.getStartTime(), unavailableDate.getEndTime())){
                        results.remove(entity);
                        break;
                    }
                }
            }
        }
        return results;
    }

    private boolean isDateRangeTaken(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime unavailableStartDate,LocalDateTime unavailableEndDate) {
        LocalDateTime onlyStartDate = unavailableStartDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime onlyEndDate = unavailableEndDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return (onlyStartDate.isBefore(startDate) && onlyEndDate.isAfter(startDate))
                || (onlyStartDate.isBefore(endDate) && onlyEndDate.isAfter(endDate))
                || (onlyEndDate.isEqual(startDate) && unavailableEndDate.getHour() >= 21)
                || (onlyStartDate.isEqual(endDate) && unavailableStartDate.getHour() <= 9);
    }
}
