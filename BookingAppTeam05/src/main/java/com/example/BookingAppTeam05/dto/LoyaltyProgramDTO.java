package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.LoyaltyProgram;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class LoyaltyProgramDTO {
    @NotNull
    @Min(1)
    @Max(1000)
    private Integer bronzeLimit;

    @NotNull
    @Min(1)
    @Max(1000)
    private Integer silverLimit;

    @NotNull
    @Min(1)
    @Max(1000)
    private Integer goldLimit;

    @NotNull
    @Min(0)
    @Max(50)
    private Double clientBronzeDiscount;

    @NotNull
    @Min(0)
    @Max(50)
    private Double clientSilverDiscount;

    @NotNull
    @Min(0)
    @Max(50)
    private Double clientGoldDiscount;

    @NotNull
    @Min(0)
    @Max(50)
    private Double ownerBronzeBonus;

    @NotNull
    @Min(0)
    @Max(50)
    private Double ownerSilverBonus;

    @NotNull
    @Min(0)
    @Max(50)
    private Double ownerGoldBonus;

    @NotNull
    @Min(1)
    @Max(1000)
    private Integer clientPointsPerReservation;

    @NotNull
    @Min(1)
    @Max(1000)
    private Integer ownerPointsPerReservation;

    private LocalDateTime startDate;

    public LoyaltyProgramDTO() {}

    public LoyaltyProgramDTO(@NotNull @Min(1) @Max(1000) Integer bronzeLimit, @NotNull @Min(1) @Max(1000) Integer silverLimit, @NotNull @Min(1) @Max(1000) Integer goldLimit, @NotNull @Min(0) @Max(50) Double clientBronzeDiscount, @NotNull @Min(0) @Max(50) Double clientSilverDiscount, @NotNull @Min(0) @Max(50) Double clientGoldDiscount, @NotNull @Min(0) @Max(50) Double ownerBronzeBonus, @NotNull @Min(0) @Max(50) Double ownerSilverBonus, @NotNull @Min(0) @Max(50) Double ownerGoldBonus, @NotNull @Min(1) @Max(1000) Integer clientPointsPerReservation, @NotNull @Min(1) @Max(1000) Integer ownerPointsPerReservation, LocalDateTime startDate) {
        this.bronzeLimit = bronzeLimit;
        this.silverLimit = silverLimit;
        this.goldLimit = goldLimit;
        this.clientBronzeDiscount = clientBronzeDiscount;
        this.clientSilverDiscount = clientSilverDiscount;
        this.clientGoldDiscount = clientGoldDiscount;
        this.ownerBronzeBonus = ownerBronzeBonus;
        this.ownerSilverBonus = ownerSilverBonus;
        this.ownerGoldBonus = ownerGoldBonus;
        this.clientPointsPerReservation = clientPointsPerReservation;
        this.ownerPointsPerReservation = ownerPointsPerReservation;
        this.startDate = startDate;
    }

    public LoyaltyProgramDTO(LoyaltyProgram loyaltyProgram) {
        this.bronzeLimit = loyaltyProgram.getBronzeLimit();
        this.silverLimit = loyaltyProgram.getSilverLimit();
        this.goldLimit = loyaltyProgram.getGoldLimit();
        this.clientBronzeDiscount = loyaltyProgram.getClientBronzeDiscount();
        this.clientSilverDiscount = loyaltyProgram.getClientSilverDiscount();
        this.clientGoldDiscount = loyaltyProgram.getClientGoldDiscount();
        this.ownerBronzeBonus = loyaltyProgram.getOwnerBronzeBonus();
        this.ownerSilverBonus = loyaltyProgram.getOwnerSilverBonus();
        this.ownerGoldBonus = loyaltyProgram.getOwnerGoldBonus();
        this.clientPointsPerReservation = loyaltyProgram.getClientPointsPerReservation();
        this.ownerPointsPerReservation = loyaltyProgram.getOwnerPointsPerReservation();
        this.startDate = loyaltyProgram.getStartDate();
    }

    public Integer getBronzeLimit() {
        return bronzeLimit;
    }

    public void setBronzeLimit(Integer bronzeLimit) {
        this.bronzeLimit = bronzeLimit;
    }

    public Integer getSilverLimit() {
        return silverLimit;
    }

    public void setSilverLimit(Integer silverLimit) {
        this.silverLimit = silverLimit;
    }

    public Integer getGoldLimit() {
        return goldLimit;
    }

    public void setGoldLimit(Integer goldLimit) {
        this.goldLimit = goldLimit;
    }

    public Double getClientBronzeDiscount() {
        return clientBronzeDiscount;
    }

    public void setClientBronzeDiscount(Double clientBronzeDiscount) {
        this.clientBronzeDiscount = clientBronzeDiscount;
    }

    public Double getClientSilverDiscount() {
        return clientSilverDiscount;
    }

    public void setClientSilverDiscount(Double clientSilverDiscount) {
        this.clientSilverDiscount = clientSilverDiscount;
    }

    public Double getClientGoldDiscount() {
        return clientGoldDiscount;
    }

    public void setClientGoldDiscount(Double clientGoldDiscount) {
        this.clientGoldDiscount = clientGoldDiscount;
    }

    public Double getOwnerBronzeBonus() {
        return ownerBronzeBonus;
    }

    public void setOwnerBronzeBonus(Double ownerBronzeBonus) {
        this.ownerBronzeBonus = ownerBronzeBonus;
    }

    public Double getOwnerSilverBonus() {
        return ownerSilverBonus;
    }

    public void setOwnerSilverBonus(Double ownerSilverBonus) {
        this.ownerSilverBonus = ownerSilverBonus;
    }

    public Double getOwnerGoldBonus() {
        return ownerGoldBonus;
    }

    public void setOwnerGoldBonus(Double ownerGoldBonus) {
        this.ownerGoldBonus = ownerGoldBonus;
    }

    public Integer getClientPointsPerReservation() {
        return clientPointsPerReservation;
    }

    public void setClientPointsPerReservation(Integer clientPointsPerReservation) {
        this.clientPointsPerReservation = clientPointsPerReservation;
    }

    public Integer getOwnerPointsPerReservation() {
        return ownerPointsPerReservation;
    }

    public void setOwnerPointsPerReservation(Integer ownerPointsPerReservation) {
        this.ownerPointsPerReservation = ownerPointsPerReservation;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
