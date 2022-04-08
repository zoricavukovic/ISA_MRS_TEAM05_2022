package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="cottageOwners")
public class CottageOwner extends User {

   @OneToMany(mappedBy = "cottageOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   public Set<Cottage> cottages;
   
   public CottageOwner() {}

   public CottageOwner(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, String password, int loyaltyPoints, boolean accountAllowed, Place place, UserType userType, LoyaltyProgram loyaltyProgram, Set<Cottage> cottages) {
      super(email, firstName, lastName, dateOfBirth, address, phoneNumber, password, loyaltyPoints, accountAllowed, place, userType, loyaltyProgram);
      this.cottages = cottages;
   }

   public Set<Cottage> getCottages() {
      return cottages;
   }

   public void setCottages(Set<Cottage> cottages) {
      this.cottages = cottages;
   }

}