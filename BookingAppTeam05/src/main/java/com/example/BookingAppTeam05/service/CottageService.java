package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.User;
import com.example.BookingAppTeam05.repository.CottageRepository;
import com.example.BookingAppTeam05.repository.PricelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CottageService {
    private CottageRepository cottageRepository;

    @Autowired
    public CottageService(CottageRepository cottageRepository) {
        this.cottageRepository = cottageRepository;
    }

    public Cottage getCottageById(Long id) {
        return cottageRepository.getCottageById(id);
    }
    public Cottage getCottageByIdWithRooms(Long id){return cottageRepository.getCottageByIdWithRooms(id);}
    public List<Cottage> getCottagesByOwnerId(Long id) {
        return cottageRepository.getCottagesByOwnerId(id);
    }

    public List<Cottage> findAll() { return cottageRepository.findAll();    }

    //public Cottage getCottageRoomWithNum(int roomNum, Long cottageId){ return cottageRepository.getCottageRoomByNum(roomNum, cottageId);}

    public Cottage save(Cottage cottage) {
        return cottageRepository.save(cottage);
    }


}
