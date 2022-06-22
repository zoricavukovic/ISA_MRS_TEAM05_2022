package com.example.BookingAppTeam05.dto;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class NewFishingEquipmentDTO {
    @NotBlank
    private String equipmentName;

    public NewFishingEquipmentDTO() {}

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}
