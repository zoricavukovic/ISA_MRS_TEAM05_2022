package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="complaints")
public class Complaint {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="description", nullable = false)
   private String description;

   @OneToOne(fetch = FetchType.LAZY)
   public Reservation reservation;

   public Complaint() {}

   public Complaint(String description, Reservation reservation) {
      this.description = description;
      this.reservation = reservation;
   }

   public Long getId() {
      return id;
   }

   public String getDescription() {
      return description;
   }

   public Reservation getReservation() {
      return reservation;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setReservation(Reservation reservation) {
      this.reservation = reservation;
   }
}