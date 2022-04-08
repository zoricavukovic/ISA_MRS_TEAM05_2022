package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.NavigationEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NavigationEquipmentService {
    @Autowired
    private NavigationEquipmentRepository navigationEquipmentRepository;
}
