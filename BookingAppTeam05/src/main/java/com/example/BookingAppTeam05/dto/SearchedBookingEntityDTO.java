package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.EntityType;

import java.util.ArrayList;
import java.util.List;

public class SearchedBookingEntityDTO {
    private Long id;
    private String name;
    private String address;
    private Place place;
    private String promoDescription;
    private double entityPricePerPerson;
    private Float averageRating;
    private List<String> pictures;
    private EntityType entityType;
    private Long ownerId;

    public SearchedBookingEntityDTO() {}

    public SearchedBookingEntityDTO(BookingEntity bookingEntity) {
        this.id = bookingEntity.getId();
        this.name = bookingEntity.getName();
        this.address = bookingEntity.getAddress();
        this.place = bookingEntity.getPlace();
        this.promoDescription = bookingEntity.getPromoDescription();
        this.pictures = new ArrayList<>();
        this.entityType = bookingEntity.getEntityType();
        bookingEntity.getPictures().forEach(p -> this.pictures.add(p.getPicturePath()));
    }

    public SearchedBookingEntityDTO(String name, String address, Place place, String promoDescription, double entityPricePerPerson, Float averageRating, List<String> pictures, EntityType entityType) {
        this.name = name;
        this.address = address;
        this.place = place;
        this.promoDescription = promoDescription;
        this.entityPricePerPerson = entityPricePerPerson;
        this.averageRating = averageRating;
        this.pictures = pictures;
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
    }

    public double getEntityPricePerPerson() {
        return entityPricePerPerson;
    }

    public void setEntityPricePerPerson(double entityPricePerPerson) {
        this.entityPricePerPerson = entityPricePerPerson;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }
}
