package com.example.BookingAppTeam05.dto;

import java.time.LocalDateTime;

public class SearchParamsForEntity {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long placeId;
    private Long numOfPersons;

    SearchParamsForEntity(){

    }

    public SearchParamsForEntity(LocalDateTime startDate, LocalDateTime endDate, Long placeId, Long numOfPersons) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.placeId = placeId;
        this.numOfPersons = numOfPersons;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Long getNumOfPersons() {
        return numOfPersons;
    }

    public void setNumOfPersons(Long numOfPersons) {
        this.numOfPersons = numOfPersons;
    }
}
