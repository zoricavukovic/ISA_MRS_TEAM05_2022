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
   public Set<Cottage> cottages = new HashSet<>();
   
   public CottageOwner() {}

   public Set<Cottage> getCottages() {
      return cottages;
   }

   public void setCottages(Set<Cottage> cottages) {
      this.cottages = cottages;
   }

}