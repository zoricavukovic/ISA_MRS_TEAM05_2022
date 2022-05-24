package com.example.BookingAppTeam05.model.repository;

import com.example.BookingAppTeam05.model.FishingEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FishingEquipmentRepository extends JpaRepository<FishingEquipment, Long> {

    public FishingEquipment findFishingEquipmentByEquipmentName(String equipmentName);

    @Query(value = "DELETE FROM adventure_fishing_equipment WHERE adventure_entity_id = ?1 AND fishing_equipment_id = ?2",nativeQuery = true)
    public void deleteFishingEqupmentIdForAdventureId(Long adventureId, Long equipmentId);

}
