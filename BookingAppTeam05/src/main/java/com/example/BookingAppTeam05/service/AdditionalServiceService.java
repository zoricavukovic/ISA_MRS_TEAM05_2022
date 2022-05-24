package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.repository.AdditionalServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalServiceService {
    private AdditionalServiceRepository additionalServiceRepository;

    @Autowired
    public AdditionalServiceService(AdditionalServiceRepository additionalServiceRepository){
        this.additionalServiceRepository = additionalServiceRepository;
    }

    public AdditionalService getAddServiceById(Long id){
        return additionalServiceRepository.getById(id);
    }

    public List<AdditionalService> findAdditionalServicesByReservationId(Long resId){return additionalServiceRepository.findAdditionalServicesByReservationId(resId);}

    public List<AdditionalService> saveAll(List<AdditionalService> ad) {
        return additionalServiceRepository.saveAll(ad);
    }

    public AdditionalService save(AdditionalService as){
        return  additionalServiceRepository.save(as);
    }

}

