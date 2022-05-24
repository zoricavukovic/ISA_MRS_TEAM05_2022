package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewFishingEquipmentDTO;
import com.example.BookingAppTeam05.model.FishingEquipment;
import com.example.BookingAppTeam05.model.repository.FishingEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FishingEquipmentService {
    @Autowired
    private FishingEquipmentRepository fishingEquipmentRepository;

    public FishingEquipmentService(FishingEquipmentRepository fishingEquipmentRepository) {
        this.fishingEquipmentRepository = fishingEquipmentRepository;
    }


    public Set<FishingEquipment> createEquipmentFromDTOArray(List<NewFishingEquipmentDTO> fishingEquipment) {
        Set<FishingEquipment> retVal = new HashSet<>();
        for (NewFishingEquipmentDTO f : fishingEquipment) {

            FishingEquipment fResult = fishingEquipmentRepository.findFishingEquipmentByEquipmentName(f.getEquipmentName());
            if (fResult != null) {
                retVal.add(fResult);
            } else {
                retVal.add(new FishingEquipment(f.getEquipmentName()));
            }
        }
        return retVal;
    }

    public Set<FishingEquipment> createEquipmentFromDTO(Set<FishingEquipment> fishingEquipment) {
        Set<FishingEquipment> retVal = new HashSet<>();
        for (FishingEquipment f : fishingEquipment) {

            FishingEquipment fResult = fishingEquipmentRepository.findFishingEquipmentByEquipmentName(f.getEquipmentName());
            if (fResult != null) {
                retVal.add(fResult);
            } else {
                retVal.add(new FishingEquipment(f.getEquipmentName()));
            }
        }
        return retVal;
    }

    public void deleteAdventureFishingEquipment(Long adventureId, Long fishingEquipmentId) {
        fishingEquipmentRepository.deleteFishingEqupmentIdForAdventureId(adventureId, fishingEquipmentId);
    }
}
