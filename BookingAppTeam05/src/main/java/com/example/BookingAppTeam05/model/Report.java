/***********************************************************************
 * Module:  Report.java
 * Author:  cr007
 * Purpose: Defines the Class Report
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Report {
   private int id;
   private String comment;
   private boolean penalizeClient;
   private boolean processed;
   
   public Reservation reservation;
   
   public String getComment() {
      return comment;
   }
   
   /** @param newComment */
   public void setComment(String newComment) {
      comment = newComment;
   }
   
   public boolean getPenalizeClient() {
      return penalizeClient;
   }
   
   /** @param newPenalizeClient */
   public void setPenalizeClient(boolean newPenalizeClient) {
      penalizeClient = newPenalizeClient;
   }
   
   public boolean getProcessed() {
      return processed;
   }
   
   /** @param newProcessed */
   public void setProcessed(boolean newProcessed) {
      processed = newProcessed;
   }
   
   public int getId() {
      return id;
   }
   
   /** @param newId */
   public void setId(int newId) {
      id = newId;
   }
   
   
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