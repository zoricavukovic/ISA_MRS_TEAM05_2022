package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.NavigationEquipment;
import com.example.BookingAppTeam05.repository.NavigationEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class NavigationEquipmentService {

    private NavigationEquipmentRepository navigationEquipmentRepository;

    @Autowired
    public NavigationEquipmentService(NavigationEquipmentRepository navigationEquipmentRepository){
        this.navigationEquipmentRepository = navigationEquipmentRepository;
    }

    public NavigationEquipmentService(){}

    @Transactional(readOnly = false)
    public void deleteNavigationEquipment(Long navId){
        this.navigationEquipmentRepository.deleteById(navId);
    }
    @Transactional(readOnly = false)
    @Modifying
    public void save(NavigationEquipment nav) {
        this.navigationEquipmentRepository.save(nav);
    }
}
