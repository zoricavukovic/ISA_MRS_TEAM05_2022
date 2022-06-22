package com.example.BookingAppTeam05.model.users;

import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Adventure;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="instructors")
@SQLDelete(sql = "UPDATE instructors SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Instructor extends User {

   @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   private Set<Adventure> adventures = new HashSet<>();

   private String reason;

   public Instructor() {}

   public Instructor(String email, String firstName, String lastName, String address, String phoneNumber, String password, boolean notYetActivated, Place place, Role role) {
      super(email, firstName, lastName, address, phoneNumber, password, notYetActivated, place, role);
   }

   public Instructor(String email, String firstName, String lastName, String address, LocalDate dateOfBirth, String phoneNumber,
                     String password, boolean notYetActivated, Place place, Role role, String reason) {
      super(email, firstName, lastName, address, dateOfBirth, phoneNumber, password, notYetActivated, place, role);
      this.reason = reason;
   }

   public Set<Adventure> getAdventures() {
      return adventures;
   }

   public void setAdventures(Set<Adventure> adventures) {
      this.adventures = adventures;
   }

   public String getReason() {
      return reason;
   }

   public void setReason(String reason) {
      this.reason = reason;
   }
}