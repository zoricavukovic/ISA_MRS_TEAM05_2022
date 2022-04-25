package com.example.BookingAppTeam05.model.users;


import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="clients")
@SQLDelete(sql = "UPDATE clients SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Client extends User {

   @Column(name="penalties")
   private int penalties;

   @ManyToMany(mappedBy = "subscribedClients", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JsonIgnore
   private Set<BookingEntity>watchedEntities = new HashSet<>();

   @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   private Set<Reservation> reservations = new HashSet<>();

   public Client(){
   }

   public int getPenalties() {
      return penalties;
   }

   public Set<BookingEntity> getWatchedEntities() {
      return watchedEntities;
   }

   public Set<Reservation> getReservations() {
      return reservations;
   }

   public void setPenalties(int penalties) {
      this.penalties = penalties;
   }

   public void setWatchedEntities(Set<BookingEntity> watchedEntities) {
      this.watchedEntities = watchedEntities;
   }

   public void setReservations(Set<Reservation> reservations) {
      this.reservations = reservations;
   }

}