package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewAdditionalServiceDTO;
import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.repository.AdditionalServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdditionalServiceService {
    private AdditionalServiceRepository additionalServiceRepository;

    @Autowired
    public AdditionalServiceService(AdditionalServiceRepository additionalServiceRepository){
        this.additionalServiceRepository = additionalServiceRepository;
    }

    public AdditionalServiceService(){}

    public AdditionalService findAdditionalServiceById(Long id){ return additionalServiceRepository.findById(id).orElse(null); }

    public List<AdditionalService> findAdditionalServicesByReservationId(Long resId){return additionalServiceRepository.findAdditionalServicesByReservationId(resId);}

    public AdditionalService save(AdditionalService as){
        return  additionalServiceRepository.save(as);
    }

    public List<NewAdditionalServiceDTO> findAdditionalServicesDTOByReservationId(Long resId) {
        List<AdditionalService> additionalServicesFound = findAdditionalServicesByReservationId(resId);
        List<NewAdditionalServiceDTO> additionalServicesDTOs = new ArrayList<>();
        for (AdditionalService additionalService:additionalServicesFound) {
            NewAdditionalServiceDTO aDTO = new NewAdditionalServiceDTO(additionalService.getId(), additionalService.getServiceName(), additionalService.getPrice());
            additionalServicesDTOs.add(aDTO);
        }
        return additionalServicesDTOs;
    }

    public AdditionalService findAdditionalServiceByName(String serviceName) {
        return additionalServiceRepository.findByServiceName(serviceName).orElse(null);
    }
}

