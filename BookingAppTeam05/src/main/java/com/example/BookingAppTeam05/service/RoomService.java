package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    public void deleteById(Long id){this.roomRepository.deleteById(id);}
}
