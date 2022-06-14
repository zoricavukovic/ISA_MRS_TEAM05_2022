package com.example.BookingAppTeam05.dto.entities;

import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ShipOwnerDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Ship;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShipDTO extends BookingEntityDTO {
    private String shipType;
    @Valid
    @NotNull
    @Min(1)
    @Max(500)
    private float length;
    @NotBlank
    private String engineNum;
    @Valid
    @NotNull
    @Min(1)
    @Max(10000)
    private int enginePower;
    @Valid
    @NotNull
    @Min(1)
    @Max(300)
    private int maxSpeed;
    @Valid
    @NotNull
    @Min(1)
    @Max(100)
    private int maxNumOfPersons;
    private Set<FishingEquipment> fishingEquipment;
    private Set<NavigationEquipment> navigationalEquipment;
    private ShipOwnerDTO shipOwner;
    @Valid
    private List<NewImageDTO> images = new ArrayList<>();
    private int version;

    public ShipDTO(){}

    public ShipDTO(Ship ship) {
        super(ship);
        this.shipType = ship.getShipType();
        this.length = ship.getLength();
        this.engineNum = ship.getEngineNum();
        this.enginePower = ship.getEnginePower();
        this.maxSpeed = ship.getMaxSpeed();
        this.maxNumOfPersons = ship.getMaxNumOfPersons();
        this.version = ship.getVersion();
    }

    public void setFetchedProperties(Ship ship){
        super.setFetchedProperties(ship);
        this.fishingEquipment = ship.getFishingEquipment();
        this.navigationalEquipment = ship.getNavigationalEquipment();
        this.shipOwner = new ShipOwnerDTO(ship.getShipOwner());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public ShipOwnerDTO getShipOwner() {
        return shipOwner;
    }

    public void setShipOwner(ShipOwnerDTO shipOwner) {
        this.shipOwner = shipOwner;
    }

    public List<NewImageDTO> getImages() {
        return images;
    }

    public void setImages(List<NewImageDTO> images) {
        this.images = images;
    }
}
