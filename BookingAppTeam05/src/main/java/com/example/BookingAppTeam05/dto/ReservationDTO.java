package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ReservationDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDate;
    @NotNull
    @Min(1)
    @Max(100)
    private int numOfDays;
    @NotNull
    @Min(1)
    @Max(30)
    private int numOfPersons;
    private Set<NewAdditionalServiceDTO> additionalServices = new HashSet<>();
    private boolean fastReservation;
    private BookingEntityDTO bookingEntity;
    private boolean canceled;
    @NotNull
    @Min(1)
    @Max(100000)
    private double cost;
    private ClientDTO client;
    private int version;

    public ReservationDTO() {
    }

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.startDate = reservation.getStartDate();
        this.numOfDays = reservation.getNumOfDays();
        this.numOfPersons = reservation.getNumOfPersons();
        this.fastReservation = reservation.isFastReservation();
        this.canceled = reservation.isCanceled();
        this.version = reservation.getVersion();
        this.cost = reservation.getCost();
    }

    public void setFetchedProperties(Reservation r) {
        this.additionalServices = new HashSet<>();
        for (AdditionalService as:r.getAdditionalServices()) {
            this.additionalServices.add(new NewAdditionalServiceDTO(as));
        }
        this.bookingEntity = new BookingEntityDTO(r.getBookingEntity());
        if(r.getClient() == null)
            this.client = null;
        else
            this.client = new ClientDTO(r.getClient());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    public int getNumOfPersons() {
        return numOfPersons;
    }

    public void setNumOfPersons(int numOfPersons) {
        this.numOfPersons = numOfPersons;
    }

    public Set<NewAdditionalServiceDTO> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(Set<NewAdditionalServiceDTO> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public boolean isFastReservation() {
        return fastReservation;
    }

    public void setFastReservation(boolean fastReservation) {
        this.fastReservation = fastReservation;
    }

    public BookingEntityDTO getBookingEntity() {
        return bookingEntity;
    }

    public void setBookingEntity(BookingEntityDTO bookingEntity) {
        this.bookingEntity = bookingEntity;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


}
