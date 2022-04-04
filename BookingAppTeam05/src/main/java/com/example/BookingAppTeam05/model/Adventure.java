/***********************************************************************
 * Module:  Adventure.java
 * Author:  cr007
 * Purpose: Defines the Class Adventure
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Adventure extends Entity {
   private String shortBio;
   private int maxNumOfPersons;
   private ArrayList<String> fishingEquipment;
   
   public Instructor instructor;
   
   public String getShortBio() {
      return shortBio;
   }
   
   /** @param newShortBio */
   public void setShortBio(String newShortBio) {
      shortBio = newShortBio;
   }
   
   public int getMaxNumOfPersons() {
      return maxNumOfPersons;
   }
   
   /** @param newMaxNumOfPersons */
   public void setMaxNumOfPersons(int newMaxNumOfPersons) {
      maxNumOfPersons = newMaxNumOfPersons;
   }
   
   public ArrayList<String> getFishingEquipment() {
      return fishingEquipment;
   }
   
   /** @param newFishingEquipment */
   public void setFishingEquipment(ArrayList<String> newFishingEquipment) {
      fishingEquipment = newFishingEquipment;
   }
   
   
   /** @pdGenerated default parent getter */
   public Instructor getInstructor() {
      return instructor;
   }
   
   /** @pdGenerated default parent setter
     * @param newInstructor */
   public void setInstructor(Instructor newInstructor) {
      if (this.instructor == null || !this.instructor.equals(newInstructor))
      {
         if (this.instructor != null)
         {
            Instructor oldInstructor = this.instructor;
            this.instructor = null;
            oldInstructor.removeAdventures(this);
         }
         if (newInstructor != null)
         {
            this.instructor = newInstructor;
            this.instructor.addAdventures(this);
         }
      }
   }

}