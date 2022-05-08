package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CottageDTO;
import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.dto.ShipDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ShipService {

    private ShipRepository shipRepository;
    private RuleOfConductService ruleOfConductService;
    private PictureService pictureService;
    private ReservationService reservationService;
    private NavigationEquipmentService navigationEquipmentService;

    @Autowired
    public ShipService(ShipRepository shipRepository, RuleOfConductService ruleOfConductService,
                       PictureService pictureService, ReservationService reservationService, NavigationEquipmentService navigationEquipmentService){
        this.shipRepository = shipRepository;
        this.ruleOfConductService = ruleOfConductService;
        this.pictureService = pictureService;
        this.reservationService = reservationService;
        this.navigationEquipmentService = navigationEquipmentService;
    }

    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    public List<Ship> getShipsByOwnerId(Long id) {
        return shipRepository.getShipsByOwnerId(id);
    }

    public User getShipOwnerOfShipId(Long id) {
        return this.shipRepository.getShipOwnerOfShipId(id);
    }
    public Ship getShipById(Long id) {
        return shipRepository.getShipById(id);
    }

    public Ship save(Ship ship) {
        return shipRepository.save(ship);
    }

    public boolean checkExistActiveReservations(Long id) {
        List<Reservation> reservations = reservationService.findAllActiveReservationsForEntity(id);
        for (Reservation r : reservations){
            if ((r.getStartDate().plusDays(r.getNumOfDays())).isAfter(LocalDateTime.now())) return true;
        }
        return false;
    }

    public Ship findShipByShipIdWithOwner(Long shipId) { return shipRepository.findShipByShipIdWithOwner(shipId);}


    public void setNewImages(Ship existingShip, List<NewImageDTO> images) {
        Set<Picture> pictures = new HashSet<>();

        for (Picture currentPicture : existingShip.getPictures()) {
            boolean found = false;
            for (NewImageDTO newImage : images) {
                if (newImage.getImageName().equals(currentPicture.getPicturePath())) {
                    found = true;
                    pictures.add(currentPicture);
                    break;
                }
            }
            if (!found) {
                pictureService.deletePictureByName(currentPicture.getPicturePath());
            }
        }
        for (NewImageDTO newImage : images) {
            boolean found = false;
            for (Picture picture : pictures) {
                if (picture.getPicturePath().equals(newImage.getImageName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                pictureService.tryToSaveNewPictureAndAddToOtherPictures(pictures, newImage);
            }
        }
        existingShip.setPictures(pictures);
    }

    public void tryToEditShipRulesOfConduct(ShipDTO shipDTO, Ship oldShip, Set<RuleOfConduct> rules) {
        for (RuleOfConduct oldRule: oldShip.getRulesOfConduct()) {
            boolean found = false;
            for (RuleOfConduct rule: shipDTO.getRulesOfConduct()){
                if (rule.getRuleName().equals(oldRule.getRuleName())){

                    if (rule.isAllowed() != oldRule.isAllowed()) {
                        ruleOfConductService.updateAllowedRuleById(oldRule.getId(), !oldRule.isAllowed());
                        oldRule.setAllowed(rule.isAllowed());
                        rules.add(oldRule);
                    }
                    else{
                        rules.add(oldRule);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                ruleOfConductService.deleteRuleById(oldRule.getId()); }

        }
        for (RuleOfConduct rule: shipDTO.getRulesOfConduct()){
            boolean found = false;
            for (RuleOfConduct addedRule: rules){
                if (rule.getRuleName().equals(addedRule.getRuleName())){
                    found = true;
                    break;
                }
            }
            if (!found) { rules.add(rule); }
        }
    }

    public void tryToEditNavigationEquipment(ShipDTO shipDTO, Ship oldShip, Set<NavigationEquipment> navigationEquipments) {
        for (NavigationEquipment oldNav: oldShip.getNavigationalEquipment()) {
            boolean found = false;
            for (NavigationEquipment nav: shipDTO.getNavigationalEquipment()){
                if (nav.getName().equals(oldNav.getName())){
                    navigationEquipments.add(oldNav);
                    navigationEquipmentService.save(oldNav);
                    found = true;
                    break;
                }
            }
            if (!found) {
                navigationEquipmentService.deleteNavigationEquipment(oldNav.getId());
            }
        }
        for (NavigationEquipment rule: shipDTO.getNavigationalEquipment()){
            boolean found = false;
            for (NavigationEquipment addedNav: navigationEquipments){
                if (rule.getName().equals(addedNav.getName())){
                    found = true;
                    break;
                }
            }
            if (!found) {
                navigationEquipments.add(rule);
                navigationEquipmentService.save(rule);
            }
        }
    }
}
