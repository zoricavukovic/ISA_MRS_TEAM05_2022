/***********************************************************************
 * Module:  ShipOwner.java
 * Author:  cr007
 * Purpose: Defines the Class ShipOwner
 ***********************************************************************/

package com.example.BookingAppTeam05.model.users;

import com.example.BookingAppTeam05.dto.users.ShipOwnerDTO;
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

   @Column(name="captain")
   private boolean captain;

   private String reason;

   public ShipOwner() {}

   public ShipOwner(String email, String firstName, String lastName, String address, String phoneNumber, String password, boolean notYetActivated, Place place, Role role, boolean captain) {
      super(email, firstName, lastName, address, phoneNumber, password, notYetActivated, place, role);
      this.captain = captain;
   }

   public ShipOwner(String email, String firstName, String lastName, String address, LocalDate dateOfBirth, String phoneNumber,
                    String password, boolean notYetActivated, Place place, Role role, boolean captain, String reason) {
      super(email, firstName, lastName, address, dateOfBirth, phoneNumber, password, notYetActivated, place, role);
      this.captain = captain;
      this.reason = reason;
   }

   public Set<Ship> getShips() {
      return ships;
   }

   public void setShips(Set<Ship> ships) {
      this.ships = ships;
   }

   public boolean isCaptain() {
      return captain;
   }

   public void setCaptain(boolean captain) {
      this.captain = captain;
   }

   public String getReason() {
      return reason;
   }

   public void setReason(String reason) {
      this.reason = reason;
   }
}