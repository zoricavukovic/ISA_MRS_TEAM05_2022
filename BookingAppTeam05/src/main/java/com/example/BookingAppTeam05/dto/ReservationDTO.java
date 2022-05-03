package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.AdditionalService;
import com.example.BookingAppTeam05.model.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ReservationDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull
    @Min(0)
    @Max(100)
    private int numOfDays;
    @NotNull
    @Min(0)
    @Max(100)
    private int numOfPersons;
    private Set<AdditionalService> additionalServices = new HashSet<>();
    private boolean fastReservation;
    private BookingEntityDTO bookingEntity;
    private boolean canceled;
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

    public Set<AdditionalService> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(Set<AdditionalService> additionalServices) {
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
}
