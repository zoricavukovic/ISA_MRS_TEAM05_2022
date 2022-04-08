package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="reports")
public class Report {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="comment", nullable = false)
   private String comment;

   @Column(name="penalizeClient", nullable = false)
   private boolean penalizeClient;

   @Column(name="processed", nullable = false)
   private boolean processed;

   @OneToOne(fetch = FetchType.LAZY)
   public Reservation reservation;

   public Report(String comment, boolean penalizeClient, boolean processed, Reservation reservation) {
      this.comment = comment;
      this.penalizeClient = penalizeClient;
      this.processed = processed;
      this.reservation = reservation;
   }

   public Report(){ }

   public Long getId() {
      return id;
   }

   public String getComment() {
      return comment;
   }

   public boolean isPenalizeClient() {
      return penalizeClient;
   }

   public boolean isProcessed() {
      return processed;
   }

   public Reservation getReservation() {
      return reservation;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public void setPenalizeClient(boolean penalizeClient) {
      this.penalizeClient = penalizeClient;
   }

   public void setProcessed(boolean processed) {
      this.processed = processed;
   }

   public void setReservation(Reservation reservation) {
      this.reservation = reservation;
   }
}