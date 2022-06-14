package com.example.BookingAppTeam05.dto.entities;

import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.InstructorDTO;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.FishingEquipment;

import java.util.Set;

public class AdventureDTO extends BookingEntityDTO {
    private String shortBio;
    private int maxNumOfPersons;
    private Set<FishingEquipment> fishingEquipment;
    private InstructorDTO instructor;
    private int version;

    public AdventureDTO() {
    }

    public AdventureDTO(Adventure adventure) {
        super(adventure);
        this.shortBio = adventure.getShortBio();
        this.maxNumOfPersons = adventure.getMaxNumOfPersons();
        this.version = adventure.getVersion();
    }

    public void setFetchedProperties(Adventure adventure){
        super.setFetchedProperties(adventure);
        this.fishingEquipment = adventure.getFishingEquipment();
        this.instructor = new InstructorDTO(adventure.getInstructor());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public int getMaxNumOfPersons() {
        return maxNumOfPersons;
    }

    public void setMaxNumOfPersons(int maxNumOfPersons) {
        this.maxNumOfPersons = maxNumOfPersons;
    }

    public Set<FishingEquipment> getFishingEquipment() {
        return fishingEquipment;
    }

    public void setFishingEquipment(Set<FishingEquipment> fishingEquipment) {
        this.fishingEquipment = fishingEquipment;
    }

    public InstructorDTO getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorDTO instructor) {
        this.instructor = instructor;
    }
}
