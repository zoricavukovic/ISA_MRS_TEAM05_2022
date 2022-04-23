package com.example.BookingAppTeam05.dto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NewAdventureDTO {
    @NotNull
    private Long instructorId;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotNull
    private Long placeId;

    @Min(1)
    @Max(100000)
    private Double costPerNight;

    @Min(1)
    @Max(100)
    private Integer maxNumOfPersons;

    @NotNull
    private String promoDescription;

    @NotNull
    private String shortBio;

    @Min(0)
    @Max(100)
    private Float entityCancelationRate;

    @Valid
    @NotNull
    private List<NewAdditionalServiceDTO> additionalServices;

    @Valid
    @NotNull
    private List<NewFishingEquipmentDTO> fishingEquipment;

    @Valid
    @NotNull
    private List<NewRuleOfConductDTO> rulesOfConduct;

    @Valid
    @NotNull
    private List<NewImageDTO> images;

    public NewAdventureDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
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

    public Double getCostPerNight() {
        return costPerNight;
    }

    public void setCostPerNight(Double costPerNight) {
        this.costPerNight = costPerNight;
    }

    public Integer getMaxNumOfPersons() {
        return maxNumOfPersons;
    }

    public void setMaxNumOfPersons(Integer maxNumOfPersons) {
        this.maxNumOfPersons = maxNumOfPersons;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public Float getEntityCancelationRate() {
        return entityCancelationRate;
    }

    public void setEntityCancelationRate(float entityCancelationRate) {
        this.entityCancelationRate = entityCancelationRate;
    }

    public List<NewAdditionalServiceDTO> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<NewAdditionalServiceDTO> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public List<NewFishingEquipmentDTO> getFishingEquipment() {
        return fishingEquipment;
    }

    public void setFishingEquipment(List<NewFishingEquipmentDTO> fishingEquipment) {
        this.fishingEquipment = fishingEquipment;
    }

    public List<NewRuleOfConductDTO> getRulesOfConduct() {
        return rulesOfConduct;
    }

    public void setRulesOfConduct(List<NewRuleOfConductDTO> rulesOfConduct) {
        this.rulesOfConduct = rulesOfConduct;
    }

    public List<NewImageDTO> getImages() {
        return images;
    }

    public void setImages(List<NewImageDTO> images) {
        this.images = images;
    }
}
