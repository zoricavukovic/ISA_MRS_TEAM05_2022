package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="adventures")
public class Adventure extends BookingEntity {

   @Column(name = "shortBio")
   private String shortBio;

   @Column(name = "maxNumOfPersons", nullable = false)
   private int maxNumOfPersons;

   @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = "adventure_fishing_equipment", joinColumns = @JoinColumn(name = "adventure_entity_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "fishing_equipment_id", referencedColumnName = "id"))
   private Set<FishingEquipment> fishingEquipment;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "instructor_id")
   public Instructor instructor;

   public Adventure() {}


   public Adventure(String promoDescription, Set<Picture> pictures, String address, String name, Set<UnavailableDate> unavailableDates, float entityCancelationRate, EntityType entityType, Set<Pricelist> pricelists, Place place, Set<RuleOfConduct> rulesOfConduct, Set<Client> subscribedClients, String shortBio, int maxNumOfPersons, Set<FishingEquipment> fishingEquipment, Instructor instructor) {
      super(promoDescription, pictures, address, name, unavailableDates, entityCancelationRate, entityType, pricelists, place, rulesOfConduct, subscribedClients);
      this.shortBio = shortBio;
      this.maxNumOfPersons = maxNumOfPersons;
      this.fishingEquipment = fishingEquipment;
      this.instructor = instructor;
   }

   public Adventure(String promoDescription, String address, String name, float entityCancelationRate, String shortBio, int maxNumOfPersons) {
      super(promoDescription, address, name, entityCancelationRate, EntityType.ADVENTURE);
      this.shortBio = shortBio;
      this.maxNumOfPersons = maxNumOfPersons;
   }


   public String getShortBio() {
      return shortBio;
   }

   public int getMaxNumOfPersons() {
      return maxNumOfPersons;
   }

   public Set<FishingEquipment> getFishingEquipment() {
      return fishingEquipment;
   }

   public Instructor getInstructor() {
      return instructor;
   }

   public void setShortBio(String shortBio) {
      this.shortBio = shortBio;
   }

   public void setMaxNumOfPersons(int maxNumOfPersons) {
      this.maxNumOfPersons = maxNumOfPersons;
   }

   public void setFishingEquipment(Set<FishingEquipment> fishingEquipment) {
      this.fishingEquipment = fishingEquipment;
   }

   public void setInstructor(Instructor instructor) {
      this.instructor = instructor;
   }
}