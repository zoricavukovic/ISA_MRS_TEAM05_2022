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
   @JsonIgnore
   private Set<Reservation> reservations = new HashSet<>();

   public Client(){
   }

   public Client(String email, String firstName, String lastName, String address, String phoneNumber, String password, boolean notYetActivated, Place place, Role role, int penalties) {
      super(email, firstName, lastName, address, phoneNumber, password, notYetActivated, place, role);
      this.penalties = penalties;
   }

   public Client(String email, String firstName, String lastName, String address, LocalDate dateOfBirth, String phoneNumber, String password, boolean notYetActivated, Place place, Role role, int penalties) {
      super(email, firstName, lastName, address, dateOfBirth, phoneNumber, password, notYetActivated, place, role);
      this.penalties = penalties;
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