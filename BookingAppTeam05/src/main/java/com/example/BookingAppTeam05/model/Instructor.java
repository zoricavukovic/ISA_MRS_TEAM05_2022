package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="instructors")
public class Instructor extends User {

   @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   public Set<Adventure> adventures;

   public Instructor() {}

   public Instructor(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, String password, int loyaltyPoints, boolean accountAllowed, Place place, UserType userType, LoyaltyProgram loyaltyProgram, Set<Adventure> adventures) {
      super(email, firstName, lastName, dateOfBirth, address, phoneNumber, password, loyaltyPoints, accountAllowed, place, userType, loyaltyProgram);
      this.adventures = adventures;
   }

   public Set<Adventure> getAdventures() {
      return adventures;
   }

   public void setAdventures(Set<Adventure> adventures) {
      this.adventures = adventures;
   }
}