package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.Place;

public class PlaceDTO {

    private Long id;
    private String zipCode;
    private String cityName;
    private String stateName;

    public PlaceDTO(){

    }

    public PlaceDTO(Place place){
        this.id = place.getId();
        this.zipCode = place.getZipCode();
        this.stateName = place.getStateName();
        this.cityName = place.getCityName();

    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
