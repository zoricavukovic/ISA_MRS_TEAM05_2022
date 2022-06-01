package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.Pricelist;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PricelistDTO {
    private Long id;
    @Min(1)
    @Max(100000)
    private double entityPricePerPerson;
    private LocalDateTime startDate;
    private Set<NewAdditionalServiceDTO> additionalServices = new HashSet<>();
    private BookingEntity bookingEntity;

    public PricelistDTO() {
    }
    public PricelistDTO(Pricelist pl){
        this.id = pl.getId();
        this.entityPricePerPerson = pl.getEntityPricePerPerson();
        this.startDate = pl.getStartDate();
    }

    public void setFetchedProperties(Pricelist pricelist){
        for(AdditionalService ad:pricelist.getAdditionalServices())
            this.additionalServices.add(new NewAdditionalServiceDTO(ad));
        // this.bookingEntity = pricelist.getBookingEntity();
    }

    public Long getId() {
        return id;
    }

    public double getEntityPricePerPerson() {
        return entityPricePerPerson;
    }

    public void setEntityPricePerPerson(double entityPricePerPerson) {
        this.entityPricePerPerson = entityPricePerPerson;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Set<NewAdditionalServiceDTO> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(Set<NewAdditionalServiceDTO> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public BookingEntity getBookingEntity() {
        return bookingEntity;
    }

    public void setBookingEntity(BookingEntity bookingEntity) {
        this.bookingEntity = bookingEntity;
    }
}
