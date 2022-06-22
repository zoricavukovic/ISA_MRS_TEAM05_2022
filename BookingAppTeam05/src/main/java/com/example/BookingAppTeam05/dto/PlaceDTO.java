package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.Place;

public class PlaceDTO {

    private Long id;
    private String zipCode;
    private String cityName;
    private String stateName;
    private double longitude;
    private double lat;

    public PlaceDTO(){

    }

    public PlaceDTO(Place place){
        this.zipCode = place.getZipCode();
        this.stateName = place.getStateName();
        this.cityName = place.getCityName();
        this.id = place.getId();
        this.longitude = place.getLongitude();
        this.lat = place.getLat();
    }

    public Long getId() {
        return id;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
