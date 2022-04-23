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
   public Set<Adventure> adventures = new HashSet<>();

   public Instructor() {}


   public Set<Adventure> getAdventures() {
      return adventures;
   }

   public void setAdventures(Set<Adventure> adventures) {
      this.adventures = adventures;
   }
}