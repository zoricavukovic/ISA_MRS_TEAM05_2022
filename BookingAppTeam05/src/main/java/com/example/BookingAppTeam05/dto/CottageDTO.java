package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.*;

import java.util.Set;

public class CottageDTO {

    private Long id;
    private Set<RuleOfConduct> rulesOfConduct;
    private Place place;
    private Set<Pricelist> pricelists;
    private EntityType entityType;
    private float entityCancelationRate;
    private String address;
    private String name;
    private String promoDescription;
    private Set<Room> rooms;

    public CottageDTO() {
    }

    public CottageDTO(Cottage cottage) {
        this.id = cottage.getId();
        this.promoDescription = cottage.getPromoDescription();
        //this.pictures = pictures;
        this.address = cottage.getAddress();
        this.name = cottage.getName();
        this.entityCancelationRate = cottage.getEntityCancelationRate();
        this.entityType = cottage.getEntityType();
        this.rooms = cottage.getRooms();
    }

    public Long getId() {
        return id;
    }

    public Set<RuleOfConduct> getRulesOfConduct() {
        return rulesOfConduct;
    }

    public void setRulesOfConduct(Set<RuleOfConduct> rulesOfConduct) {
        this.rulesOfConduct = rulesOfConduct;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Set<Pricelist> getPricelists() {
        return pricelists;
    }

    public void setPricelists(Set<Pricelist> pricelists) {
        this.pricelists = pricelists;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public float getEntityCancelationRate() {
        return entityCancelationRate;
    }

    public void setEntityCancelationRate(float entityCancelationRate) {
        this.entityCancelationRate = entityCancelationRate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }
}
