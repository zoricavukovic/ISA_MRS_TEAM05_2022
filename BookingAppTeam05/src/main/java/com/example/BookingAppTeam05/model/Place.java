/***********************************************************************
 * Module:  Place.java
 * Author:  cr007
 * Purpose: Defines the Class Place
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Place {
   private String zipCode;
   private String cityName;
   private String stateName;
   
   public String getZipCode() {
      return zipCode;
   }
   
   public String getCityName() {
      return cityName;
   }
   
   public String getStateName() {
      return stateName;
   }
   
   /** @param newZipCode */
   public void setZipCode(String newZipCode) {
      zipCode = newZipCode;
   }
   
   /** @param newCityName */
   public void setCityName(String newCityName) {
      cityName = newCityName;
   }
   
   /** @param newStateName */
   public void setStateName(String newStateName) {
      stateName = newStateName;
   }

}