package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CottageDTO;
import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.dto.ShipDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
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

    @Autowired
    public ShipService(ShipRepository shipRepository, RuleOfConductService ruleOfConductService,
                       PictureService pictureService, ReservationService reservationService){
        this.shipRepository = shipRepository;
        this.ruleOfConductService = ruleOfConductService;
        this.pictureService = pictureService;
        this.reservationService = reservationService;
    }

    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    public List<Ship> getShipsByOwnerId(Long id) {
        return shipRepository.getShipsByOwnerId(id);
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

}
