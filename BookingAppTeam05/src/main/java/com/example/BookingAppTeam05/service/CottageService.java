package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CottageService {
    private CottageRepository cottageRepository;
    private ReservationService reservationService;
    private PictureService pictureService;

    @Autowired
    public CottageService(CottageRepository cottageRepository, ReservationService reservationService, PictureService pictureService) {
        this.cottageRepository = cottageRepository;
        this.reservationService = reservationService;
        this.pictureService = pictureService;
    }

    public Cottage getCottageById(Long id) {
        return cottageRepository.getCottageById(id);
    }
    public Cottage getCottageByIdWithRooms(Long id){return cottageRepository.getCottageByIdWithRooms(id);}
    public List<Cottage> getCottagesByOwnerId(Long id) {
        return cottageRepository.getCottagesByOwnerId(id);
    }
    
    public boolean logicalDeleteCottageById(Long id){
        if (checkExistActiveReservations(id)) return false;
        cottageRepository.logicalDeleteById(id);
        return true;
    }
    public boolean checkExistActiveReservations(Long id) {
        List<Reservation> reservations = reservationService.findAllActiveReservationsForCottage(id);
        for (Reservation r : reservations){
           if ((r.getStartDate().plusDays(r.getNumOfDays())).isAfter(LocalDateTime.now())) return true;
        }
        return false;
    }

    public List<Cottage> findAll() { return cottageRepository.findAll();    }

    //public Cottage getCottageRoomWithNum(int roomNum, Long cottageId){ return cottageRepository.getCottageRoomByNum(roomNum, cottageId);}

    public Cottage save(Cottage cottage) {
        return cottageRepository.save(cottage);
    }

    public List<Cottage> searchCottagesOfOwner(Long ownerId, String cottageName, String firstOp, String city, String secondOp, float rate) {

        if (firstOp.equals("AND") && secondOp.equals("AND")){
            return cottageRepository.searchCottagesOfOwnerAndAnd(ownerId, cottageName, city, rate);
        }
        else if (firstOp.equals("AND") && secondOp.equals("OR")) {
            return cottageRepository.searchCottagesOfOwnerAndOr(ownerId, cottageName, city, rate);
        }
        else if (firstOp.equals("OR") && secondOp.equals("AND")) {
            return cottageRepository.searchCottagesOfOwnerOrAnd(ownerId, cottageName, city, rate);
        }
        else {
            return cottageRepository.searchCottagesOfOwnerOrOr(ownerId, cottageName, city, rate);
        }
    }
    public Cottage findCottageByCottageIdWithOwner(Long cottageId) { return cottageRepository.findCottageByCottageIdWithOwner(cottageId);}

    public void setNewImages(Cottage existingCottage, Set<Picture> images) {
        Set<Picture> pictures = new HashSet<>();

        for (Picture currentPicture : existingCottage.getPictures()) {
            boolean found = false;
            for (Picture newPicture : images) {
                System.out.println(newPicture.getPicturePath());
                if (newPicture.getPicturePath().equals(currentPicture.getPicturePath())) {
                    found = true;
                    pictures.add(currentPicture);
                    break;
                }
            }
            if (!found) {
                pictureService.deletePictureByName(currentPicture.getPicturePath());
            }
        }
        for (Picture newImage : images) {
            boolean found = false;
            for (Picture picture : pictures) {
                if (picture.getPicturePath().equals(newImage.getPicturePath())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                //pictureService.tryToSaveNewPictureAndAddToOtherPictures(pictures, newImage);
            }
        }
        existingCottage.setPictures(pictures);

    }
}
