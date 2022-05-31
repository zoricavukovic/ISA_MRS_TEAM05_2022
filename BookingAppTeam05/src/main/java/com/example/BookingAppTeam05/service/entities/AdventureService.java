package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.users.NewAdventureDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.model.repository.entities.AdventureRepository;
import com.example.BookingAppTeam05.service.FishingEquipmentService;
import com.example.BookingAppTeam05.service.PictureService;
import com.example.BookingAppTeam05.service.PricelistService;
import com.example.BookingAppTeam05.service.RuleOfConductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdventureService {
    private AdventureRepository adventureRepository;
    private PricelistService pricelistService;
    private FishingEquipmentService fishingEquipmentService;
    private RuleOfConductService ruleOfConductService;
    private PictureService pictureService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public AdventureService(AdventureRepository adventureRepository,
                            RuleOfConductService ruleOfConductService,
                            PricelistService pricelistService,
                            FishingEquipmentService fishingEquipmentService,
                            PictureService pictureService,
                            @Lazy BookingEntityService bookingEntityService)
    {
        this.adventureRepository = adventureRepository;
        this.pricelistService = pricelistService;
        this.ruleOfConductService = ruleOfConductService;
        this.fishingEquipmentService = fishingEquipmentService;
        this.pictureService = pictureService;
        this.bookingEntityService = bookingEntityService;
    }

    public Adventure getAdventureById(Long id) {
        return this.adventureRepository.getAdventureById(id);
    }

    public Adventure getAdventureByIdCanBeDeleted(Long id) {
        return this.adventureRepository.getAdventureByIdCanBeDeleted(id);
    }

    public List<Adventure> findAll() {
        return this.adventureRepository.findAll();
    }

    public List<Adventure> findAllForOwnerId(Long id) {
        List<Adventure> all = findAll();
        List<Adventure> retVal = new ArrayList<>();
        for (Adventure a : all) {
            if (a.getInstructor().getId().equals(id))
                retVal.add(a);
        }
        return retVal;
    }

    public List<Adventure> getAdventuresByOwnerId(Long id) {
        return this.adventureRepository.getAdventuresForOwnerId(id);
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

        Pricelist createNewPricelist = pricelistService.createPrilistFromDTO(newAdventureDTO.getAdditionalServices(), newAdventureDTO.getCostPerPerson());
        adventure.addPriceList(createNewPricelist);
        adventure = adventureRepository.save(adventure);
        return adventure;
    }

    public Adventure editAdventureById(Long id, NewAdventureDTO newAdventureDTO, Place place) {
        Adventure existingAdventure = adventureRepository.getAdventureById(id);
        existingAdventure.setName(newAdventureDTO.getName());
        existingAdventure.setAddress(newAdventureDTO.getAddress());
        existingAdventure.setMaxNumOfPersons(newAdventureDTO.getMaxNumOfPersons());
        existingAdventure.setShortBio(newAdventureDTO.getShortBio());
        existingAdventure.setEntityCancelationRate(newAdventureDTO.getEntityCancelationRate());
        existingAdventure.setPromoDescription(newAdventureDTO.getPromoDescription());

        updateRulesOfConductForAdventure(existingAdventure, newAdventureDTO.getRulesOfConduct());
        Set<FishingEquipment> fishingEquipment = fishingEquipmentService.createEquipmentFromDTOArray(newAdventureDTO.getFishingEquipment());
        existingAdventure.setFishingEquipment(fishingEquipment);
        setNewPricelistIfNeeded(existingAdventure, newAdventureDTO);
        bookingEntityService.setNewImagesForBookingEntity(existingAdventure, newAdventureDTO.getImages());
        existingAdventure = adventureRepository.save(existingAdventure);
        return existingAdventure;
    }

    private void setNewPricelistIfNeeded(Adventure adventure, NewAdventureDTO newAdventureDTO) {
        Pricelist pricelist = pricelistService.getCurrentPricelistForEntityId(adventure.getId());
        double newCostPerPerson = newAdventureDTO.getCostPerPerson();
        Pricelist createNewPricelist = null;
        if (pricelist.getEntityPricePerPerson() != newCostPerPerson) {
            createNewPricelist = pricelistService.createPrilistFromDTO(newAdventureDTO.getAdditionalServices(), newAdventureDTO.getCostPerPerson());
            adventure.addPriceList(createNewPricelist);
            return;
        }
        for (AdditionalService currService : pricelist.getAdditionalServices()) {
            boolean found = false;
            for (NewAdditionalServiceDTO newService : newAdventureDTO.getAdditionalServices()) {
                if (newService.getServiceName().equals(currService.getServiceName()) &&
                        newService.getPrice() == currService.getPrice()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                createNewPricelist = pricelistService.createPrilistFromDTO(newAdventureDTO.getAdditionalServices(), newAdventureDTO.getCostPerPerson());
                adventure.addPriceList(createNewPricelist);
                return;
            }
        }
    }

    private void updateRulesOfConductForAdventure(Adventure adventure, List<NewRuleOfConductDTO> newRules) {
        Set<RuleOfConduct> updatedRules = new HashSet<>();

        for (RuleOfConduct oldRule: adventure.getRulesOfConduct()) {
            boolean found = false;
            for (NewRuleOfConductDTO newRule: newRules) {
                if (newRule.getRuleName().equals(oldRule.getRuleName())) {
                    if (newRule.getAllowed() != oldRule.isAllowed()) {
                        ruleOfConductService.updateAllowedRuleById(oldRule.getId(), !oldRule.isAllowed());
                        oldRule.setAllowed(newRule.getAllowed());
                    }
                    updatedRules.add(oldRule);
                    found = true;
                    break;
                }
            }
            if (!found) {
                ruleOfConductService.deleteRuleById(oldRule.getId());
            }
        }

        for (NewRuleOfConductDTO newRule: newRules){
            boolean found = false;
            for (RuleOfConduct addedRule: updatedRules ){
                if (newRule.getRuleName().equals(addedRule.getRuleName())){
                    found = true;
                    break;
                }
            }
            if (!found) { updatedRules.add(new RuleOfConduct(newRule.getRuleName(), newRule.getAllowed())); }
        }
        adventure.setRulesOfConduct(updatedRules);
    }

    public User getInstructorOfAdventureId(Long id) {
        return this.adventureRepository.getInstructorOfAdventureId(id);
    }

    public Adventure findById(Long id) {
        Optional<Adventure> adventure = adventureRepository.findById(id);
        return adventure.orElse(null);
    }

}
