package com.example.BookingAppTeam05.dto;

public class SimpleSearchForBookingEntityDTO {
    private Long ownerId;
    private String name;
    private String address;
    private Long placeId;
    private Double minCostPerPerson;
    private Double maxCostPerPerson;
    private Float minRating;

    public SimpleSearchForBookingEntityDTO() {}

    public SimpleSearchForBookingEntityDTO(String name, String address, Long placeId, Double minCostPerPerson, Double maxCostPerPerson, Float minRating) {
        this.name = name;
        this.address = address;
        this.placeId = placeId;
        this.minCostPerPerson = minCostPerPerson;
        this.maxCostPerPerson = maxCostPerPerson;
        this.minRating = minRating;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Double getMinCostPerPerson() {
        return minCostPerPerson;
    }

    public void setMinCostPerPerson(Double minCostPerPerson) {
        this.minCostPerPerson = minCostPerPerson;
    }

    public Double getMaxCostPerPerson() {
        return maxCostPerPerson;
    }

    public void setMaxCostPerPerson(Double maxCostPerPerson) {
        this.maxCostPerPerson = maxCostPerPerson;
    }

    public Float getMinRating() {
        return minRating;
    }

    public void setMinRating(Float minRating) {
        this.minRating = minRating;
    }
}
