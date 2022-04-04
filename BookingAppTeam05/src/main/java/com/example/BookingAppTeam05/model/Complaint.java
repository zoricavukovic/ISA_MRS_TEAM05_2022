/***********************************************************************
 * Module:  Complaint.java
 * Author:  cr007
 * Purpose: Defines the Class Complaint
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Complaint {
   private int id;
   private String description;
   
   public Reservation reservation;
   
   
   /** @pdGenerated default parent getter */
   public Reservation getReservation() {
      return reservation;
   }
   
   /** @pdGenerated default parent setter
     * @param newReservation */
   public void setReservation(Reservation newReservation) {
      this.reservation = newReservation;
   }

}