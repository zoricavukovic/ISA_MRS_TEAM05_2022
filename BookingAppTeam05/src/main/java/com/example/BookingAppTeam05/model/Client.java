package com.example.BookingAppTeam05.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="clients")
public class Client extends User {

   @Column(name="penalties")
   private int penalties;

   @ManyToMany(mappedBy = "subscribedClients", fetch = FetchType.LAZY)
   private Set<BookingEntity>watchedEntities;

   @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   private Set<Reservation> reservations;

   public Client(){
   }

   public Client(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, String password, int loyaltyPoints, boolean accountAllowed, Place place, UserType userType, LoyaltyProgram loyaltyProgram, int penalties, Set<BookingEntity> watchedEntities, Set<Reservation> reservations) {
      super(email, firstName, lastName, dateOfBirth, address, phoneNumber, password, loyaltyPoints, accountAllowed, place, userType, loyaltyProgram);
      this.penalties = penalties;
      this.watchedEntities = watchedEntities;
      this.reservations = reservations;
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