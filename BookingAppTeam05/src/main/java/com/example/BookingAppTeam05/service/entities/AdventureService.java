package com.example.BookingAppTeam05.service.entities;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.users.NewAdventureDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.model.repository.entities.AdventureRepository;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdventureService {
    private AdventureRepository adventureRepository;
    private PricelistService pricelistService;
    private FishingEquipmentService fishingEquipmentService;
    private RuleOfConductService ruleOfConductService;
    private PictureService pictureService;
    private PlaceService placeService;
    private InstructorService instructorService;
    private ReservationService reservationService;

    @Autowired
    public AdventureService(AdventureRepository adventureRepository,
                            RuleOfConductService ruleOfConductService,
                            PricelistService pricelistService,
                            FishingEquipmentService fishingEquipmentService,
                            PictureService pictureService,
                            PlaceService placeService,
                            InstructorService instructorService,
                            ReservationService reservationService)
    {
        this.adventureRepository = adventureRepository;
        this.pricelistService = pricelistService;
        this.ruleOfConductService = ruleOfConductService;
        this.fishingEquipmentService = fishingEquipmentService;
        this.pictureService = pictureService;
        this.placeService = placeService;
        this.instructorService = instructorService;
        this.reservationService = reservationService;
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

    @Transactional
    public String createAdventure(NewAdventureDTO newAdventureDTO){
        Place place = placeService.getPlaceById(newAdventureDTO.getPlaceId());
        if (place == null) {
            return "Cant find place with id: " + newAdventureDTO.getPlaceId();
        }
        Instructor instructor = instructorService.findById(newAdventureDTO.getInstructorId());
        if (instructor == null) {
            return "Cant find instructor with id: " + newAdventureDTO.getInstructorId();
        }
        Adventure newAdventure = createNewAdventure(newAdventureDTO, place, instructor);
        if (newAdventure == null) {
            return "Error. Cant create adventure";
        }
        return newAdventure.getId().toString();
    }

    public String updateAdventure(NewAdventureDTO newAdventureDTO, Long id){
        try{
            if (reservationService.findAllActiveReservationsForEntityid(id).size() != 0)
                return "Cant update adventure because there exist active reservations";

            Place place = placeService.getPlaceById(newAdventureDTO.getPlaceId());
            if (place == null) {
                return "Cant find place with id: " + newAdventureDTO.getPlaceId();
            }
            Instructor instructor = instructorService.findById(newAdventureDTO.getInstructorId());
            if (instructor == null) {
                return "Cant find instructor with id: " + newAdventureDTO.getInstructorId();
            }
            Adventure adventure = getAdventureById(id);
            if (adventure == null) {
                return "Cant find adventure with id: " + id;
            }
            if (adventure.getVersion() != newAdventureDTO.getVersion()) return "Conflict seems to have occurred, someone changed your adventure data before you. Please refresh page and try again";

            editAdventureById(id, newAdventureDTO, place);
            return "";
        } catch (ObjectOptimisticLockingFailureException e) {
            return "Conflict seems to have occurred, someone changed your adventure before you. Please refresh page and try again";
        }
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
        adventure.setVersion(0);
        adventure.setLocked(false);
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
        pictureService.setNewImagesForBookingEntity(existingAdventure, newAdventureDTO.getImages());
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
