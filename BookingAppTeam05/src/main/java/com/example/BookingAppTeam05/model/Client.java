/***********************************************************************
 * Module:  Client.java
 * Author:  cr007
 * Purpose: Defines the Class Client
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Client extends User {
   private int penalties;
   
   public List<Entity> watchedEntities;
   public List<Reservation> reservations;
   
   
   /** @pdGenerated default getter */
   public List<Entity> getWatchedEntities() {
      if (watchedEntities == null)
         watchedEntities = new ArrayList<Entity>();
      return watchedEntities;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorWatchedEntities() {
      if (watchedEntities == null)
         watchedEntities = new ArrayList<Entity>();
      return watchedEntities.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newWatchedEntities */
   public void setWatchedEntities(List<Entity> newWatchedEntities) {
      removeAllWatchedEntities();
      for (Iterator iter = newWatchedEntities.iterator(); iter.hasNext();)
         addWatchedEntities((Entity)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newEntity */
   public void addWatchedEntities(Entity newEntity) {
      if (newEntity == null)
         return;
      if (this.watchedEntities == null)
         this.watchedEntities = new ArrayList<Entity>();
      if (!this.watchedEntities.contains(newEntity))
      {
         this.watchedEntities.add(newEntity);
         newEntity.addSubscribedClients(this);      
      }
   }
   
   /** @pdGenerated default remove
     * @param oldEntity */
   public void removeWatchedEntities(Entity oldEntity) {
      if (oldEntity == null)
         return;
      if (this.watchedEntities != null)
         if (this.watchedEntities.contains(oldEntity))
         {
            this.watchedEntities.remove(oldEntity);
            oldEntity.removeSubscribedClients(this);
         }
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllWatchedEntities() {
      if (watchedEntities != null)
      {
         Entity oldEntity;
         for (Iterator iter = getIteratorWatchedEntities(); iter.hasNext();)
         {
            oldEntity = (Entity)iter.next();
            iter.remove();
            oldEntity.removeSubscribedClients(this);
         }
      }
   }
   /** @pdGenerated default getter */
   public List<Reservation> getReservations() {
      if (reservations == null)
         reservations = new ArrayList<Reservation>();
      return reservations;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorReservations() {
      if (reservations == null)
         reservations = new ArrayList<Reservation>();
      return reservations.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newReservations */
   public void setReservations(List<Reservation> newReservations) {
      removeAllReservations();
      for (Iterator iter = newReservations.iterator(); iter.hasNext();)
         addReservations((Reservation)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newReservation */
   public void addReservations(Reservation newReservation) {
      if (newReservation == null)
         return;
      if (this.reservations == null)
         this.reservations = new ArrayList<Reservation>();
      if (!this.reservations.contains(newReservation))
      {
         this.reservations.add(newReservation);
         newReservation.setClient(this);      
      }
   }
   
   /** @pdGenerated default remove
     * @param oldReservation */
   public void removeReservations(Reservation oldReservation) {
      if (oldReservation == null)
         return;
      if (this.reservations != null)
         if (this.reservations.contains(oldReservation))
         {
            this.reservations.remove(oldReservation);
            oldReservation.setClient((Client)null);
         }
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllReservations() {
      if (reservations != null)
      {
         Reservation oldReservation;
         for (Iterator iter = getIteratorReservations(); iter.hasNext();)
         {
            oldReservation = (Reservation)iter.next();
            iter.remove();
            oldReservation.setClient((Client)null);
         }
      }
   }

}