package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.repository.CottageRepository;
import com.example.BookingAppTeam05.repository.PricelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PricelistService {
    private PricelistRepository pricelistRepository;

    @Autowired
    public PricelistService(PricelistRepository pricelistRepository) {
        this.pricelistRepository = pricelistRepository;
    }

    public List<Pricelist> getCurrentPricelistByBookingEntityId(Long id) {
        return pricelistRepository.getCurrentPricelistByBookingEntityId(id);
    }


    public Pricelist createPrilistFromDTO(List<NewAdditionalServiceDTO> additionalServices, Double costPerNight) {
        Pricelist retVal = new Pricelist();
        if (!additionalServices.isEmpty()) {
            Set<AdditionalService> additionalServiceSet = new HashSet<>();
            for (NewAdditionalServiceDTO a : additionalServices) {
                additionalServiceSet.add(new AdditionalService(a.getPrice(), a.getServiceName()));
            }
            retVal.setAdditionalServices(additionalServiceSet);
        }
        retVal.setEntityPricePerPerson(costPerNight);
        retVal.setStartDate(LocalDateTime.now());
        return retVal;
    }
}
