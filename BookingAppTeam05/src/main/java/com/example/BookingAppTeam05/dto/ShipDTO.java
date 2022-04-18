package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.*;
import java.util.Set;

public class ShipDTO extends BookingEntityDTO{


    private String shipType;
    private float length;
    private String engineNum;
    private int enginePower;
    private int maxSpeed;
    private int maxNumOfPersons;
    private Set<FishingEquipment> fishingEquipment;
    private Set<NavigationEquipment> navigationalEquipment;
    private ShipOwner shipOwner;

    public ShipDTO(){}

    public ShipDTO(Ship ship) {
        super(ship);
        this.shipType = ship.getShipType();
        this.length = ship.getLength();
        this.engineNum = ship.getEngineNum();
        this.enginePower = ship.getEnginePower();
        this.maxSpeed = ship.getMaxSpeed();
        this.maxNumOfPersons = ship.getMaxNumOfPersons();
        //this.fishingEquipment = ship.getFishingEquipment();
        //this.navigationalEquipment = ship.getNavigationalEquipment();
        //this.shipOwner = ship.getShipOwner();
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
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

    public Set<NavigationEquipment> getNavigationalEquipment() {
        return navigationalEquipment;
    }

    public void setNavigationalEquipment(Set<NavigationEquipment> navigationalEquipment) {
        this.navigationalEquipment = navigationalEquipment;
    }

    public ShipOwner getShipOwner() {
        return shipOwner;
    }

    public void setShipOwner(ShipOwner shipOwner) {
        this.shipOwner = shipOwner;
    }
}
