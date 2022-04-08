package com.example.BookingAppTeam05.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@Entity
@Table(name="places")
public class Place {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="zipCode", nullable = false, unique = true)
   private String zipCode;

   @Column(name="cityName", nullable = false)
   private String cityName;

   @Column(name="stateName", nullable = false)
   private String stateName;
   
   public Place(){ }

   public Place(String zipCode, String cityName, String stateName){
      this.zipCode = zipCode;
      this.cityName = cityName;
      this.stateName = stateName;

   }

   public Long getId() {
      return id;
   }
   public String getZipCode() {
      return zipCode;
   }

   public String getCityName() {
      return cityName;
   }

   public String getStateName() {
      return stateName;
   }

   public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
   }

   public void setCityName(String cityName) {
      this.cityName = cityName;
   }

   public void setStateName(String stateName) {
      this.stateName = stateName;
   }


}