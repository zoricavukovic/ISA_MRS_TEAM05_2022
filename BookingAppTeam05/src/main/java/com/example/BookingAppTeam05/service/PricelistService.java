package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.dto.PricelistDTO;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.database.DatabaseException;
import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.repository.PricelistRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PricelistService {
    private PricelistRepository pricelistRepository;
    private BookingEntityService bookingEntityService;

    @Autowired
    public PricelistService(PricelistRepository pricelistRepository, @Lazy BookingEntityService bookingEntityService) {
        this.pricelistRepository = pricelistRepository;
        this.bookingEntityService = bookingEntityService;
    }

    public PricelistService(){}

    public List<Pricelist> getCurrentPricelistByBookingEntityId(Long id) {
        return pricelistRepository.getCurrentPricelistByBookingEntityId(id);
    }

    public PricelistDTO getCurrentPricelistDTOByBookingEntityId(Long id) {
        List<Pricelist> pricelistList = getCurrentPricelistByBookingEntityId(id);
        if (pricelistList == null)
            throw new ItemNotFoundException("Can't find pricelist for this entity.");
        Pricelist p = pricelistList.get(0);
        PricelistDTO pricelistDTO = new PricelistDTO(p);
        Set<NewAdditionalServiceDTO> newAs = new HashSet<>();
        for(AdditionalService as:p.getAdditionalServices())
            newAs.add(new NewAdditionalServiceDTO(as));
        pricelistDTO.setAdditionalServices(newAs);
        return pricelistDTO;
    }

    public Pricelist getCurrentPricelistForEntityId(Long id) {
        return pricelistRepository.getCurrentPricelistByBookingEntityId(id).get(0);
    }

    public Pricelist save(Pricelist pricelist) {
        return pricelistRepository.save(pricelist);
    }

    public Pricelist createPricelistFromDTO(List<NewAdditionalServiceDTO> additionalServices, Double costPerNight) {
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

    public Pricelist updatePricelist(Long idBookingEntity, PricelistDTO pricelistDTO) {
        try{
            Pricelist pricelist = new Pricelist();
            pricelist.setEntityPricePerPerson(pricelistDTO.getEntityPricePerPerson());
            pricelist.setBookingEntity(bookingEntityService.getBookingEntityById(idBookingEntity));
            pricelist.setStartDate(LocalDateTime.now());
            Set<AdditionalService> additionalServices= new HashSet<>();
            for(NewAdditionalServiceDTO newAs: pricelistDTO.getAdditionalServices())
            {
                AdditionalService as = new AdditionalService();
                as.setPrice(newAs.getPrice());
                as.setServiceName(newAs.getServiceName());
                as.setId(newAs.getId());
                additionalServices.add(as);
            }
            pricelist.setAdditionalServices(additionalServices);
            save(pricelist);
            return pricelist;
        } catch (Exception ex){
            throw new DatabaseException("Can't update info about pricelist at the moment. Try again!");
        }
    }
}
