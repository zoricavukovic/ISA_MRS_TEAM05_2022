package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.Adventure;
import com.example.BookingAppTeam05.model.FishingEquipment;
import com.example.BookingAppTeam05.model.Instructor;
import com.example.BookingAppTeam05.model.Picture;

import java.util.Set;

public class AdventureDTO extends BookingEntityDTO {
    private String shortBio;
    private int maxNumOfPersons;
    private Set<FishingEquipment> fishingEquipment;
    private Instructor instructor;

    public AdventureDTO() {
    }

    public AdventureDTO(Adventure adventure) {
        super(adventure);
        this.shortBio = adventure.getShortBio();
        this.maxNumOfPersons = adventure.getMaxNumOfPersons();
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

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
