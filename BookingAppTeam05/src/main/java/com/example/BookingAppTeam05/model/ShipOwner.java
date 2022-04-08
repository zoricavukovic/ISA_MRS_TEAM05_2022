/***********************************************************************
 * Module:  ShipOwner.java
 * Author:  cr007
 * Purpose: Defines the Class ShipOwner
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="shipOwners")
public class ShipOwner extends User {

   @OneToMany(mappedBy = "shipOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   public Set<Ship> ships;

   public ShipOwner() {}

   public ShipOwner(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, String password, int loyaltyPoints, boolean accountAllowed, Place place, UserType userType, LoyaltyProgram loyaltyProgram, Set<Ship> ships) {
      super(email, firstName, lastName, dateOfBirth, address, phoneNumber, password, loyaltyPoints, accountAllowed, place, userType, loyaltyProgram);
      this.ships = ships;
   }

   public Set<Ship> getShips() {
      return ships;
   }

   public void setShips(Set<Ship> ships) {
      this.ships = ships;
   }
}