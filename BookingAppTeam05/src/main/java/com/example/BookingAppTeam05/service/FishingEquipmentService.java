package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.FishingEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FishingEquipmentService {
    @Autowired
    private FishingEquipmentRepository fishingEquipmentRepository;
}
