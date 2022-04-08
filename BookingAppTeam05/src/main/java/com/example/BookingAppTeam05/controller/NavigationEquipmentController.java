package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.NavigationEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class NavigationEquipmentController {
    @Autowired
    private NavigationEquipmentService navigationEquipmentService;
}
