package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.FishingEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FishingEquipmentController {
    @Autowired
    private FishingEquipmentService fishingEquipmentService;
}
