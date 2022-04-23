/***********************************************************************
 * Module:  ShipOwner.java
 * Author:  cr007
 * Purpose: Defines the Class ShipOwner
 ***********************************************************************/

package com.example.BookingAppTeam05.model.users;

import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Ship;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="shipOwners")
@SQLDelete(sql = "UPDATE shipOwners SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ShipOwner extends User {

   @OneToMany(mappedBy = "shipOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   public Set<Ship> ships = new HashSet<>();

   public ShipOwner() {}

   public Set<Ship> getShips() {
      return ships;
   }

   public void setShips(Set<Ship> ships) {
      this.ships = ships;
   }
}