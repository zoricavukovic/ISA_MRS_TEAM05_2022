package com.example.BookingAppTeam05.dto.entities;

import com.example.BookingAppTeam05.dto.PricelistDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.calendar.UnavailableDateDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.users.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class BookingEntityDTO {
    private Long id;
    private String promoDescription;
    @NotBlank
    private String address;
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    @Max(100)
    private float entityCancelationRate;
    public EntityType entityType;
    @NotNull
    public Place place;
    private Set<ReservationDTO> reservations;
    public Set<RuleOfConduct> rulesOfConduct;
    public Set<ClientDTO> subscribedClients;
    private Set<UnavailableDateDTO> unavailableDates;
    public Set<PricelistDTO> pricelists;
    private Set<Picture> pictures;
    private Set<LocalDateTime> allUnavailableDates;

    public BookingEntityDTO() {}
    public BookingEntityDTO(BookingEntity bookingEntity) {
        this.id = bookingEntity.getId();
        this.promoDescription = bookingEntity.getPromoDescription();
        this.address = bookingEntity.getAddress();
        this.name = bookingEntity.getName();
        this.entityCancelationRate = bookingEntity.getEntityCancelationRate();
        this.entityType = bookingEntity.getEntityType();
    }

    public void setFetchedProperties(BookingEntity entity) {
        this.place = entity.getPlace();
        this.reservations = new HashSet<>();
        for (Reservation reservation: entity.getReservations()){
            this.reservations.add(new ReservationDTO(reservation));
        }
        this.rulesOfConduct = entity.getRulesOfConduct();
        this.subscribedClients = new HashSet<>();
        for(Client client: entity.getSubscribedClients())
            this.subscribedClients.add(new ClientDTO(client));
        this.unavailableDates = new HashSet<>();
        for(UnavailableDate unavailableDate: entity.getUnavailableDates()){
            this.unavailableDates.add(new UnavailableDateDTO(unavailableDate));
        }
        this.pricelists = new HashSet<>();
        for (Pricelist pricelist: entity.getPricelists()) {
            PricelistDTO pricelistDTO = new PricelistDTO(pricelist);
            pricelistDTO.setFetchedProperties(pricelist);
            this.pricelists.add(pricelistDTO);
        }
        this.pictures = entity.getPictures();
    }

    public Long getId() {
        return id;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
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
        return this.entityCancelationRate;
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

    public Set<ReservationDTO> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationDTO> reservations) {
        this.reservations = reservations;
    }


    public Set<RuleOfConduct> getRulesOfConduct() {
        return rulesOfConduct;
    }

    public void setRulesOfConduct(Set<RuleOfConduct> rulesOfConduct) {
        this.rulesOfConduct = rulesOfConduct;
    }

    public Set<ClientDTO> getSubscribedClients() {
        return subscribedClients;
    }

    public void setSubscribedClients(Set<ClientDTO> subscribedClients) {
        this.subscribedClients = subscribedClients;
    }

    public Set<UnavailableDateDTO> getUnavailableDates() {
        return unavailableDates;
    }

    public void setUnavailableDates(Set<UnavailableDateDTO> unavailableDates) {
        this.unavailableDates = unavailableDates;
    }

    public Set<PricelistDTO> getPricelists() {
        return pricelists;
    }

    public void setPricelists(Set<PricelistDTO> pricelists) {
        this.pricelists = pricelists;
    }

    public Set<LocalDateTime> getAllUnavailableDates() {
        return allUnavailableDates;
    }

    public void setAllUnavailableDates(Set<LocalDateTime> allUnavailableDates) {
        this.allUnavailableDates = allUnavailableDates;
    }
}
