package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.FishingEquipment;
import com.example.BookingAppTeam05.model.users.Instructor;

import java.util.Set;

public class AdventureDTO extends BookingEntityDTO {
    private String shortBio;
    private int maxNumOfPersons;
    private Set<FishingEquipment> fishingEquipment;
    private InstructorDTO instructor;

    public AdventureDTO() {
    }

    public AdventureDTO(Adventure adventure) {
        super(adventure);
        this.shortBio = adventure.getShortBio();
        this.maxNumOfPersons = adventure.getMaxNumOfPersons();
    }

    public void setFetchedProperties(Adventure adventure){
        super.setFetchedProperties(adventure);
        this.fishingEquipment = adventure.getFishingEquipment();
        this.instructor = new InstructorDTO(adventure.getInstructor());
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
