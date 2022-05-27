package com.example.BookingAppTeam05.model.users;

import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Cottage;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="cottageOwners")
@SQLDelete(sql = "UPDATE cottageOwners SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class CottageOwner extends User {

   @OneToMany(mappedBy = "cottageOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   private Set<Cottage> cottages = new HashSet<>();

   private String reason;
   
   public CottageOwner() {}

   public CottageOwner(String email, String firstName, String lastName, String address, String phoneNumber, String password, boolean notYetActivated, Place place, Role role) {
      super(email, firstName, lastName, address, phoneNumber, password, notYetActivated, place, role);
   }

   public CottageOwner(String email, String firstName, String lastName, String address, LocalDate dateOfBirth, String phoneNumber, String password,
                       boolean notYetActivated, Place place, Role role, String reason) {
      super(email, firstName, lastName, address, dateOfBirth, phoneNumber, password, notYetActivated, place, role);
      this.reason = reason;
   }

   public Set<Cottage> getCottages() {
      return cottages;
   }

   public void setCottages(Set<Cottage> cottages) {
      this.cottages = cottages;
   }

   public String getReason() {
      return reason;
   }

   public void setReason(String reason) {
      this.reason = reason;
   }
}