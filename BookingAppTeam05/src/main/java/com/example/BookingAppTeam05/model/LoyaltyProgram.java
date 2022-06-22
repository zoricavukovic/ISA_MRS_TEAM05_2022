package com.example.BookingAppTeam05.model;

import com.example.BookingAppTeam05.dto.LoyaltyProgramDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyaltyPrograms")
public class LoyaltyProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="bronzeLimit")
    private Integer bronzeLimit;

    @Column(name="silverLimit")
    private Integer silverLimit;

    @Column(name="goldLimit")
    private Integer goldLimit;

    @Column(name="clientBronzeDiscount")
    private Double clientBronzeDiscount;

    @Column(name="clientSilverDiscount")
    private Double clientSilverDiscount;

    @Column(name="clientGoldDiscount")
    private Double clientGoldDiscount;

    @Column(name="ownerBronzeBonus")
    private Double ownerBronzeBonus;

    @Column(name="ownerSilverBonus")
    private Double ownerSilverBonus;

    @Column(name="ownerGoldBonus")
    private Double ownerGoldBonus;

    @Column(name="clientPointsPerReservation")
    private Integer clientPointsPerReservation;

    @Column(name="ownerPointsPerReservation")
    private Integer ownerPointsPerReservation;

    @Column(name="startDate", nullable = false)
    private LocalDateTime startDate;

    public LoyaltyProgram() {}

    public LoyaltyProgram(LoyaltyProgramDTO loyaltyProgramDTO) {
        this.bronzeLimit = loyaltyProgramDTO.getBronzeLimit();
        this.silverLimit = loyaltyProgramDTO.getSilverLimit();
        this.goldLimit = loyaltyProgramDTO.getGoldLimit();
        this.clientBronzeDiscount = loyaltyProgramDTO.getClientBronzeDiscount();
        this.clientSilverDiscount = loyaltyProgramDTO.getClientSilverDiscount();
        this.clientGoldDiscount = loyaltyProgramDTO.getClientGoldDiscount();
        this.clientPointsPerReservation = loyaltyProgramDTO.getClientPointsPerReservation();
        this.ownerPointsPerReservation = loyaltyProgramDTO.getOwnerPointsPerReservation();
        this.ownerBronzeBonus = loyaltyProgramDTO.getOwnerBronzeBonus();
        this.ownerSilverBonus = loyaltyProgramDTO.getOwnerSilverBonus();
        this.ownerGoldBonus = loyaltyProgramDTO.getOwnerGoldBonus();
        this.startDate = LocalDateTime.now();
    }

    public LoyaltyProgram(Integer bronzeLimit, Integer silverLimit, Integer goldLimit, Double clientBronzeDiscount, Double clientSilverDiscount, Double clientGoldDiscount, Double ownerBronzeBonus, Double ownerSilverBonus, Double ownerGoldBonus, Integer clientPointsPerReservation, Integer ownerPointsPerReservation, LocalDateTime startDate) {
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

    public Long getId() {
        return id;
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
