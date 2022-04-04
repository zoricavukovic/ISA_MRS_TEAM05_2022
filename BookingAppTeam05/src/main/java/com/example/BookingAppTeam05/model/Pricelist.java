/***********************************************************************
 * Module:  Pricelist.java
 * Author:  cr007
 * Purpose: Defines the Class Pricelist
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Pricelist {
   private int id;
   private double entityPricePerPerson;
   private Date startDate;
   private HashMap<String, double> additionalServices;
   
   public double getEntityPricePerPerson() {
      return entityPricePerPerson;
   }
   
   /** @param newEntityPricePerPerson */
   public void setEntityPricePerPerson(double newEntityPricePerPerson) {
      entityPricePerPerson = newEntityPricePerPerson;
   }
   
   public Date getStartDate() {
      return startDate;
   }
   
   /** @param newStartDate */
   public void setStartDate(Date newStartDate) {
      startDate = newStartDate;
   }
   
   public HashMap<String, double> getAdditionalServices() {
      return additionalServices;
   }
   
   /** @param newAdditionalServices */
   public void setAdditionalServices(HashMap<String, double> newAdditionalServices) {
      additionalServices = newAdditionalServices;
   }

}