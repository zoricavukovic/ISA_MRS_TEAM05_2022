package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewAdventureDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.repository.AdventureRepository;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AdventureService {
    private AdventureRepository adventureRepository;
    private PricelistService pricelistService;
    private FishingEquipmentService fishingEquipmentService;
    private RuleOfConductService ruleOfConductService;
    private PictureService pictureService;

    @Autowired
    public AdventureService(AdventureRepository adventureRepository,
                            RuleOfConductService ruleOfConductService,
                            PricelistService pricelistService,
                            FishingEquipmentService fishingEquipmentService,
                            PictureService pictureService)
    {
        this.adventureRepository = adventureRepository;
        this.pricelistService = pricelistService;
        this.ruleOfConductService = ruleOfConductService;
        this.fishingEquipmentService = fishingEquipmentService;
        this.pictureService = pictureService;
    }

    public Adventure getAdventureById(Long id) {
        return this.adventureRepository.getAdventureById(id);
    }


    public Adventure createNewAdventure(NewAdventureDTO newAdventureDTO, Place place, Instructor instructor) {
        Adventure adventure = new Adventure(newAdventureDTO.getPromoDescription(), newAdventureDTO.getAddress(), newAdventureDTO.getName(), newAdventureDTO.getEntityCancelationRate(), newAdventureDTO.getShortBio(), newAdventureDTO.getMaxNumOfPersons());
        adventure.setPlace(place);
        adventure.setInstructor(instructor);

        if (!newAdventureDTO.getFishingEquipment().isEmpty()) {
            Set<FishingEquipment> createdFishingEquipment = fishingEquipmentService.createEquipmentFromDTOArray(newAdventureDTO.getFishingEquipment());
            adventure.setFishingEquipment(createdFishingEquipment);
        }
        if (!newAdventureDTO.getRulesOfConduct().isEmpty()) {
            Set<RuleOfConduct> createRulesOfConduct = ruleOfConductService.createRulesFromDTOArray(newAdventureDTO.getRulesOfConduct());
            adventure.setRulesOfConduct(createRulesOfConduct);
        }
        if (!newAdventureDTO.getImages().isEmpty()) {
            Set<Picture> createdPictures = pictureService.createPicturesFromDTO(newAdventureDTO.getImages());
            adventure.setPictures(createdPictures);
        }

        Pricelist createNewPricelist = pricelistService.createPrilistFromDTO(newAdventureDTO.getAdditionalServices(), newAdventureDTO.getCostPerNight());
        adventure.addPriceList(createNewPricelist);
        adventure = adventureRepository.save(adventure);
        return adventure;
    }
}
