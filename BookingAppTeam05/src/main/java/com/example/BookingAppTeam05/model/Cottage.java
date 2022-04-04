/***********************************************************************
 * Module:  Cottage.java
 * Author:  cr007
 * Purpose: Defines the Class Cottage
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Cottage extends Entity {
   public List<Room> rooms;
   public Collection<CottageOwner> owner;
   
   
   /** @pdGenerated default getter */
   public List<Room> getRooms() {
      if (rooms == null)
         rooms = new ArrayList<Room>();
      return rooms;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorRooms() {
      if (rooms == null)
         rooms = new ArrayList<Room>();
      return rooms.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newRooms */
   public void setRooms(List<Room> newRooms) {
      removeAllRooms();
      for (Iterator iter = newRooms.iterator(); iter.hasNext();)
         addRooms((Room)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newRoom */
   public void addRooms(Room newRoom) {
      if (newRoom == null)
         return;
      if (this.rooms == null)
         this.rooms = new ArrayList<Room>();
      if (!this.rooms.contains(newRoom))
         this.rooms.add(newRoom);
   }
   
   /** @pdGenerated default remove
     * @param oldRoom */
   public void removeRooms(Room oldRoom) {
      if (oldRoom == null)
         return;
      if (this.rooms != null)
         if (this.rooms.contains(oldRoom))
            this.rooms.remove(oldRoom);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllRooms() {
      if (rooms != null)
         rooms.clear();
   }
   /** @pdGenerated default getter */
   public Collection<CottageOwner> getOwner() {
      if (owner == null)
         owner = new HashSet<CottageOwner>();
      return owner;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorOwner() {
      if (owner == null)
         owner = new HashSet<CottageOwner>();
      return owner.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newOwner */
   public void setOwner(Collection<CottageOwner> newOwner) {
      removeAllOwner();
      for (Iterator iter = newOwner.iterator(); iter.hasNext();)
         addOwner((CottageOwner)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newCottageOwner */
   public void addOwner(CottageOwner newCottageOwner) {
      if (newCottageOwner == null)
         return;
      if (this.owner == null)
         this.owner = new HashSet<CottageOwner>();
      if (!this.owner.contains(newCottageOwner))
      {
         this.owner.add(newCottageOwner);
         newCottageOwner.addCottages(this);      
      }
   }
   
   /** @pdGenerated default remove
     * @param oldCottageOwner */
   public void removeOwner(CottageOwner oldCottageOwner) {
      if (oldCottageOwner == null)
         return;
      if (this.owner != null)
         if (this.owner.contains(oldCottageOwner))
         {
            this.owner.remove(oldCottageOwner);
            oldCottageOwner.removeCottages(this);
         }
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllOwner() {
      if (owner != null)
      {
         CottageOwner oldCottageOwner;
         for (Iterator iter = getIteratorOwner(); iter.hasNext();)
         {
            oldCottageOwner = (CottageOwner)iter.next();
            iter.remove();
            oldCottageOwner.removeCottages(this);
         }
      }
   }

}