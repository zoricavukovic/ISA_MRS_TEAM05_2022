/***********************************************************************
 * Module:  Ship.java
 * Author:  cr007
 * Purpose: Defines the Class Ship
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Ship extends Entity {
   private String type;
   private float length;
   private String engineNum;
   private int enginePower;
   private int maxSpeed;
   private int maxNumOfPersons;
   private ArrayList<String> fishingEquipment;
   
   public List<NavigationEquipment> navigationalEquipment;
   public ShipOwner owner;
   
   
   /** @pdGenerated default getter */
   public List<NavigationEquipment> getNavigationalEquipment() {
      if (navigationalEquipment == null)
         navigationalEquipment = new ArrayList<NavigationEquipment>();
      return navigationalEquipment;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorNavigationalEquipment() {
      if (navigationalEquipment == null)
         navigationalEquipment = new ArrayList<NavigationEquipment>();
      return navigationalEquipment.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newNavigationalEquipment */
   public void setNavigationalEquipment(List<NavigationEquipment> newNavigationalEquipment) {
      removeAllNavigationalEquipment();
      for (Iterator iter = newNavigationalEquipment.iterator(); iter.hasNext();)
         addNavigationalEquipment((NavigationEquipment)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newNavigationEquipment */
   public void addNavigationalEquipment(NavigationEquipment newNavigationEquipment) {
      if (newNavigationEquipment == null)
         return;
      if (this.navigationalEquipment == null)
         this.navigationalEquipment = new ArrayList<NavigationEquipment>();
      if (!this.navigationalEquipment.contains(newNavigationEquipment))
         this.navigationalEquipment.add(newNavigationEquipment);
   }
   
   /** @pdGenerated default remove
     * @param oldNavigationEquipment */
   public void removeNavigationalEquipment(NavigationEquipment oldNavigationEquipment) {
      if (oldNavigationEquipment == null)
         return;
      if (this.navigationalEquipment != null)
         if (this.navigationalEquipment.contains(oldNavigationEquipment))
            this.navigationalEquipment.remove(oldNavigationEquipment);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllNavigationalEquipment() {
      if (navigationalEquipment != null)
         navigationalEquipment.clear();
   }
   /** @pdGenerated default parent getter */
   public ShipOwner getOwner() {
      return owner;
   }
   
   /** @pdGenerated default parent setter
     * @param newShipOwner */
   public void setOwner(ShipOwner newShipOwner) {
      if (this.owner == null || !this.owner.equals(newShipOwner))
      {
         if (this.owner != null)
         {
            ShipOwner oldShipOwner = this.owner;
            this.owner = null;
            oldShipOwner.removeShips(this);
         }
         if (newShipOwner != null)
         {
            this.owner = newShipOwner;
            this.owner.addShips(this);
         }
      }
   }

}