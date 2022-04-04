/***********************************************************************
 * Module:  Instructor.java
 * Author:  cr007
 * Purpose: Defines the Class Instructor
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Instructor extends User {
   public List<Adventure> adventures;
   
   
   /** @pdGenerated default getter */
   public List<Adventure> getAdventures() {
      if (adventures == null)
         adventures = new ArrayList<Adventure>();
      return adventures;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorAdventures() {
      if (adventures == null)
         adventures = new ArrayList<Adventure>();
      return adventures.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newAdventures */
   public void setAdventures(List<Adventure> newAdventures) {
      removeAllAdventures();
      for (Iterator iter = newAdventures.iterator(); iter.hasNext();)
         addAdventures((Adventure)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newAdventure */
   public void addAdventures(Adventure newAdventure) {
      if (newAdventure == null)
         return;
      if (this.adventures == null)
         this.adventures = new ArrayList<Adventure>();
      if (!this.adventures.contains(newAdventure))
      {
         this.adventures.add(newAdventure);
         newAdventure.setInstructor(this);
      }
   }
   
   /** @pdGenerated default remove
     * @param oldAdventure */
   public void removeAdventures(Adventure oldAdventure) {
      if (oldAdventure == null)
         return;
      if (this.adventures != null)
         if (this.adventures.contains(oldAdventure))
         {
            this.adventures.remove(oldAdventure);
            oldAdventure.setInstructor((Instructor)null);
         }
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllAdventures() {
      if (adventures != null)
      {
         Adventure oldAdventure;
         for (Iterator iter = getIteratorAdventures(); iter.hasNext();)
         {
            oldAdventure = (Adventure)iter.next();
            iter.remove();
            oldAdventure.setInstructor((Instructor)null);
         }
      }
   }

}