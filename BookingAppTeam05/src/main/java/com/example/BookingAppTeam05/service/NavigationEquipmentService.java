package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.NavigationEquipment;
import com.example.BookingAppTeam05.model.repository.NavigationEquipmentRepository;
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

    public Set<NavigationEquipment> createEquipmentFromDTO(Set<NavigationEquipment> navigationEquipment, Long shipId) {
        Set<NavigationEquipment> retVal = new HashSet<>();
        for (NavigationEquipment f : navigationEquipment) {
            NavigationEquipment fResult = navigationEquipmentRepository.findNavigationEquipmentByNameAndShipId(f.getName(), shipId);
            System.out.println("MOLIM TE, " + fResult.getName());
            if (fResult != null) {
                retVal.add(fResult);
            } else {
                retVal.add(new NavigationEquipment(f.getName()));
            }
        }
        return retVal;
    }

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
