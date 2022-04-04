/***********************************************************************
 * Module:  CottageOwner.java
 * Author:  cr007
 * Purpose: Defines the Class CottageOwner
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class CottageOwner extends User {
   public List<Cottage> cottages;
   
   
   /** @pdGenerated default getter */
   public List<Cottage> getCottages() {
      if (cottages == null)
         cottages = new ArrayList<Cottage>();
      return cottages;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorCottages() {
      if (cottages == null)
         cottages = new ArrayList<Cottage>();
      return cottages.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newCottages */
   public void setCottages(List<Cottage> newCottages) {
      removeAllCottages();
      for (Iterator iter = newCottages.iterator(); iter.hasNext();)
         addCottages((Cottage)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newCottage */
   public void addCottages(Cottage newCottage) {
      if (newCottage == null)
         return;
      if (this.cottages == null)
         this.cottages = new ArrayList<Cottage>();
      if (!this.cottages.contains(newCottage))
      {
         this.cottages.add(newCottage);
         newCottage.addOwner(this);      
      }
   }
   
   /** @pdGenerated default remove
     * @param oldCottage */
   public void removeCottages(Cottage oldCottage) {
      if (oldCottage == null)
         return;
      if (this.cottages != null)
         if (this.cottages.contains(oldCottage))
         {
            this.cottages.remove(oldCottage);
            oldCottage.removeOwner(this);
         }
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllCottages() {
      if (cottages != null)
      {
         Cottage oldCottage;
         for (Iterator iter = getIteratorCottages(); iter.hasNext();)
         {
            oldCottage = (Cottage)iter.next();
            iter.remove();
            oldCottage.removeOwner(this);
         }
      }
   }

}