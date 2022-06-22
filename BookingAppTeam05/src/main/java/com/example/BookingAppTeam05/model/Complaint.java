package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="complaints")
public class Complaint {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="description", nullable = false, length = 1024)
   private String description;

   @OneToOne(fetch = FetchType.LAZY)
   private Reservation reservation;

   @Column(name="processed")
   private boolean processed;

   @Column(name="adminResponse", length = 1024)
   private String adminResponse;

   @Version
   @Column(name="version", unique=false, nullable=false)
   private Long version;

   public Complaint() {}

   public Complaint(String description, Reservation reservation) {
      this.description = description;
      this.reservation = reservation;
   }

   public Long getVersion() {
      return version;
   }

   public void setVersion(Long version) {
      this.version = version;
   }

   public String getAdminResponse() {
      return adminResponse;
   }

   public void setAdminResponse(String adminResponse) {
      this.adminResponse = adminResponse;
   }

   public boolean isProcessed() {
      return processed;
   }

   public void setProcessed(boolean processed) {
      this.processed = processed;
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