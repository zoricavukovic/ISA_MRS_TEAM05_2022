package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.FishingEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FishingEquipmentRepository extends JpaRepository<FishingEquipment, Long> {

    public FishingEquipment findFishingEquipmentByEquipmentName(String equipmentName);
}
