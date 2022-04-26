package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.SimpleSearchForBookingEntityDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    public SearchService() {}

    private List<SearchedBookingEntityDTO> searchByName(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        String sName = s.getName().trim().toLowerCase();
        if (!sName.equals(""))
            result = result.stream().filter(i -> i.getName().toLowerCase().equals(sName)).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByAddress(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        String sAddress = s.getAddress().trim().toLowerCase();
        if (!sAddress.equals(""))
            result = result.stream().filter(i -> i.getAddress().toLowerCase().equals(sAddress)).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByPlaceId(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        Long placeId = s.getPlaceId();
        if (placeId != null)
            result = result.stream().filter(i -> i.getPlace().getId().equals(placeId)).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByRating(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        Float minRating = s.getMinRating();
        if (minRating != null)
            result = result.stream().filter(i -> i.getAverageRating()!=null &&  i.getAverageRating() >= minRating).collect(Collectors.toList());
        return result;
    }
    private List<SearchedBookingEntityDTO> searchByMinAndMaxPrice(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
        List<SearchedBookingEntityDTO> result = entities;
        Double minCost = s.getMinCostPerPerson();
        Double maxCost = s.getMaxCostPerPerson();

        if (minCost != null && maxCost != null) {
            result = result.stream().filter(i -> i.getEntityPricePerPerson() >= minCost && i.getEntityPricePerPerson() <= maxCost).collect(Collectors.toList());
        } else if (minCost != null) {
            result = result.stream().filter(i -> i.getEntityPricePerPerson() >= minCost).collect(Collectors.toList());
        } else if (maxCost != null) {
            result = result.stream().filter(i -> i.getEntityPricePerPerson() <= maxCost).collect(Collectors.toList());
        }
        return result;
    }

    private boolean endSearch(List<SearchedBookingEntityDTO> entities) {
        return (entities.size() == 0 || entities.size() == 1);
    }

    public List<SearchedBookingEntityDTO> simpleFilterSearchForBookingEntities(List<SearchedBookingEntityDTO> entities, SimpleSearchForBookingEntityDTO s) {
        List<SearchedBookingEntityDTO> result;
        result = searchByName(entities, s);
        if (endSearch(result)) return result;

        result = searchByAddress(result, s);
        if (endSearch(result)) return result;

        result = searchByPlaceId(result, s);
        if (endSearch(result)) return result;

        result = searchByRating(result, s);
        if (endSearch(result)) return result;

        result = searchByMinAndMaxPrice(result, s);
        return result;
    }
}
