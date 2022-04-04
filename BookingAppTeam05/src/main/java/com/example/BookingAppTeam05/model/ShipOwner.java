/***********************************************************************
 * Module:  ShipOwner.java
 * Author:  cr007
 * Purpose: Defines the Class ShipOwner
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class ShipOwner extends User {
   public List<Ship> ships;
   
   
   /** @pdGenerated default getter */
   public List<Ship> getShips() {
      if (ships == null)
         ships = new ArrayList<Ship>();
      return ships;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorShips() {
      if (ships == null)
         ships = new ArrayList<Ship>();
      return ships.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newShips */
   public void setShips(List<Ship> newShips) {
      removeAllShips();
      for (Iterator iter = newShips.iterator(); iter.hasNext();)
         addShips((Ship)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newShip */
   public void addShips(Ship newShip) {
      if (newShip == null)
         return;
      if (this.ships == null)
         this.ships = new ArrayList<Ship>();
      if (!this.ships.contains(newShip))
      {
         this.ships.add(newShip);
         newShip.setOwner(this);      
      }
   }
   
   /** @pdGenerated default remove
     * @param oldShip */
   public void removeShips(Ship oldShip) {
      if (oldShip == null)
         return;
      if (this.ships != null)
         if (this.ships.contains(oldShip))
         {
            this.ships.remove(oldShip);
            oldShip.setOwner((ShipOwner)null);
         }
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllShips() {
      if (ships != null)
      {
         Ship oldShip;
         for (Iterator iter = getIteratorShips(); iter.hasNext();)
         {
            oldShip = (Ship)iter.next();
            iter.remove();
            oldShip.setOwner((ShipOwner)null);
         }
      }
   }

}