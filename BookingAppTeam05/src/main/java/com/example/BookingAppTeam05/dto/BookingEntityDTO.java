package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.*;

import javax.persistence.*;
import java.util.Set;

public class BookingEntityDTO {
    private Long id;
    private String promoDescription;
    private String address;
    private String name;
    private float entityCancelationRate;
    public EntityType entityType;

    public Place place;
    private Set<Reservation> reservations;
    private Set<Picture> pictures;
    public Set<RuleOfConduct> rulesOfConduct;
    public Set<Client> subscribedClients;
    private Set<UnavailableDate> unavailableDates;
    public Set<Pricelist> pricelists;

    public BookingEntityDTO() {}
    public BookingEntityDTO(BookingEntity bookingEntity) {
        this.id = bookingEntity.getId();
        this.promoDescription = bookingEntity.getPromoDescription();
        this.address = bookingEntity.getAddress();
        this.name = bookingEntity.getName();
        this.entityCancelationRate = bookingEntity.getEntityCancelationRate();
        this.entityType = bookingEntity.getEntityType();
    }

    public Long getId() {
        return id;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
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

    public float getEntityCancelationRate() {
        return entityCancelationRate;
    }

    public void setEntityCancelationRate(float entityCancelationRate) {
        this.entityCancelationRate = entityCancelationRate;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public Set<RuleOfConduct> getRulesOfConduct() {
        return rulesOfConduct;
    }

    public void setRulesOfConduct(Set<RuleOfConduct> rulesOfConduct) {
        this.rulesOfConduct = rulesOfConduct;
    }

    public Set<Client> getSubscribedClients() {
        return subscribedClients;
    }

    public void setSubscribedClients(Set<Client> subscribedClients) {
        this.subscribedClients = subscribedClients;
    }

    public Set<UnavailableDate> getUnavailableDates() {
        return unavailableDates;
    }

    public void setUnavailableDates(Set<UnavailableDate> unavailableDates) {
        this.unavailableDates = unavailableDates;
    }

    public Set<Pricelist> getPricelists() {
        return pricelists;
    }

    public void setPricelists(Set<Pricelist> pricelists) {
        this.pricelists = pricelists;
    }
}
