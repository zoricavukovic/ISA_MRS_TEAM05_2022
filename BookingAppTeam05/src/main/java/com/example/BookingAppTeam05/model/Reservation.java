/***********************************************************************
 * Module:  Reservation.java
 * Author:  cr007
 * Purpose: Defines the Class Reservation
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Reservation {
   private int id;
   private Date startDate;
   private Date endDate;
   private int numOfPersons;
   private ArrayList<String> additionalServices;
   private boolean fastReservation;
   
   public Entity entity;
   public ReservationStatus reservationStatus;
   public Client client;
   
   public int getId() {
      return id;
   }
   
   /** @param newId */
   public void setId(int newId) {
      id = newId;
   }
   
   public Date getStartDate() {
      return startDate;
   }
   
   /** @param newStartDate */
   public void setStartDate(Date newStartDate) {
      startDate = newStartDate;
   }
   
   public Date getEndDate() {
      return endDate;
   }
   
   /** @param newEndDate */
   public void setEndDate(Date newEndDate) {
      endDate = newEndDate;
   }
   
   public int getNumOfPersons() {
      return numOfPersons;
   }
   
   /** @param newNumOfPersons */
   public void setNumOfPersons(int newNumOfPersons) {
      numOfPersons = newNumOfPersons;
   }
   
   public ArrayList<String> getAdditionalServices() {
      return additionalServices;
   }
   
   /** @param newAdditionalServices */
   public void setAdditionalServices(ArrayList<String> newAdditionalServices) {
      additionalServices = newAdditionalServices;
   }
   
   public int getReservationPrice() {
      // TODO: implement
      return 0;
   }
   
   public boolean getFastReservation() {
      return fastReservation;
   }
   
   /** @param newFastReservation */
   public void setFastReservation(boolean newFastReservation) {
      fastReservation = newFastReservation;
   }
   
   
   /** @pdGenerated default parent getter */
   public Entity getEntity() {
      return entity;
   }
   
   /** @pdGenerated default parent setter
     * @param newEntity */
   public void setEntity(Entity newEntity) {
      this.entity = newEntity;
   }
   /** @pdGenerated default parent getter */
   public ReservationStatus getReservationStatus() {
      return reservationStatus;
   }
   
   /** @pdGenerated default parent setter
     * @param newReservationStatus */
   public void setReservationStatus(ReservationStatus newReservationStatus) {
      this.reservationStatus = newReservationStatus;
   }
   /** @pdGenerated default parent getter */
   public Client getClient() {
      return client;
   }
   
   /** @pdGenerated default parent setter
     * @param newClient */
   public void setClient(Client newClient) {
      if (this.client == null || !this.client.equals(newClient))
      {
         if (this.client != null)
         {
            Client oldClient = this.client;
            this.client = null;
            oldClient.removeReservations(this);
         }
         if (newClient != null)
         {
            this.client = newClient;
            this.client.addReservations(this);
         }
      }
   }

}