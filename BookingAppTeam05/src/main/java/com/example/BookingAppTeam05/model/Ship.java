package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="ships")
public class Ship extends BookingEntity {
   @Column(name="shipType")
   private String shipType;

   @Column(name="length")
   private float length;

   @Column(name="engineNum")
   private String engineNum;

   @Column(name="enginePower")
   private int enginePower;

   @Column(name="maxSpeed")
   private int maxSpeed;

   @Column(name="maxNumOfPersons", nullable = false)
   private int maxNumOfPersons;

   @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = "ship_fishing_equipment", joinColumns = @JoinColumn(name = "ship_entity_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "fishing_equipment_id", referencedColumnName = "id"))
   private Set<FishingEquipment> fishingEquipment;

   @OneToMany(fetch = FetchType.LAZY)
   @JoinColumn(name="ship_id")
   public Set<NavigationEquipment> navigationalEquipment;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name="ship_owner_id")
   public ShipOwner shipOwner;
   
   public Ship() {}

   public Ship(String promoDescription, Set<Picture> pictures, String address, String name, Set<UnavailableDate> unavailableDates, float entityCancelationRate, EntityType entityType, Set<Pricelist> pricelists, Place place, Set<RuleOfConduct> rulesOfConduct, Set<Client> subscribedClients, String type, float length, String engineNum, int enginePower, int maxSpeed, int maxNumOfPersons, Set<FishingEquipment> fishingEquipment, Set<NavigationEquipment> navigationalEquipment, ShipOwner owner) {
      super(promoDescription, pictures, address, name, unavailableDates, entityCancelationRate, entityType, pricelists, place, rulesOfConduct, subscribedClients);
      this.shipType = type;
      this.length = length;
      this.engineNum = engineNum;
      this.enginePower = enginePower;
      this.maxSpeed = maxSpeed;
      this.maxNumOfPersons = maxNumOfPersons;
      this.fishingEquipment = fishingEquipment;
      this.navigationalEquipment = navigationalEquipment;
      this.shipOwner = owner;
   }

   public String getShipType() {
      return shipType;
   }

   public float getLength() {
      return length;
   }

   public String getEngineNum() {
      return engineNum;
   }

   public int getEnginePower() {
      return enginePower;
   }

   public int getMaxSpeed() {
      return maxSpeed;
   }

   public int getMaxNumOfPersons() {
      return maxNumOfPersons;
   }

   public Set<FishingEquipment> getFishingEquipment() {
      return fishingEquipment;
   }

   public Set<NavigationEquipment> getNavigationalEquipment() {
      return navigationalEquipment;
   }

   public ShipOwner getShipOwner() {
      return shipOwner;
   }

   public void setShipType(String type) {
      this.shipType = type;
   }

   public void setLength(float length) {
      this.length = length;
   }

   public void setEngineNum(String engineNum) {
      this.engineNum = engineNum;
   }

   public void setEnginePower(int enginePower) {
      this.enginePower = enginePower;
   }

   public void setMaxSpeed(int maxSpeed) {
      this.maxSpeed = maxSpeed;
   }

   public void setMaxNumOfPersons(int maxNumOfPersons) {
      this.maxNumOfPersons = maxNumOfPersons;
   }

   public void setFishingEquipment(Set<FishingEquipment> fishingEquipment) {
      this.fishingEquipment = fishingEquipment;
   }

   public void setNavigationalEquipment(Set<NavigationEquipment> navigationalEquipment) {
      this.navigationalEquipment = navigationalEquipment;
   }

   public void setShipOwner(ShipOwner owner) {
      this.shipOwner = owner;
   }
}