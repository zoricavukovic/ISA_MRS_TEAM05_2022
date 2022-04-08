package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="reservations")
public class Reservation {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

   @Column(name="startDate", nullable = false)
   private LocalDateTime startDate;

   @Column(name="numOfDays", nullable = false)
   private int numOfDays;

   @Column(name="numOfPersons", nullable = false)
   private int numOfPersons;

   @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = "reservation_additional_service", joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "additional_service_id", referencedColumnName = "id"))
   private Set<AdditionalService> additionalServices;

   @Column(name="fastReservation", nullable = false)
   private boolean fastReservation;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name="entity_id")
   private BookingEntity bookingEntity;

   @Column(name="canceled")
   private boolean canceled;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "client_id")
   private Client client;
   
   public Reservation() {}

   public Reservation(LocalDateTime startDate, int numOfDays, int numOfPersons, Set<AdditionalService> additionalServices, boolean fastReservation, BookingEntity entity, boolean canceled, Client client) {
      this.startDate = startDate;
      this.numOfPersons = numOfPersons;
      this.additionalServices = additionalServices;
      this.fastReservation = fastReservation;
      this.bookingEntity = entity;
      this.canceled = canceled;
      this.client = client;
      this.numOfDays = numOfDays;
   }

   public int getId() {
      return id;
   }

   public LocalDateTime getStartDate() {
      return startDate;
   }

   public LocalDateTime getEndDate() {
      return this.startDate.plusDays(this.numOfDays);
   }

   public int getNumOfPersons() {
      return numOfPersons;
   }

   public Set<AdditionalService> getAdditionalServices() {
      return additionalServices;
   }

   public boolean isFastReservation() {
      return fastReservation;
   }

   public BookingEntity getBookingEntity() {
      return bookingEntity;
   }


   public Client getClient() {
      return client;
   }

   public void setStartDate(LocalDateTime startDate) {
      this.startDate = startDate;
   }

   public void setNumOfPersons(int numOfPersons) {
      this.numOfPersons = numOfPersons;
   }

   public void setAdditionalServices(Set<AdditionalService> additionalServices) {
      this.additionalServices = additionalServices;
   }

   public void setFastReservation(boolean fastReservation) {
      this.fastReservation = fastReservation;
   }

   public void setBookingEntity(BookingEntity entity) {
      this.bookingEntity = entity;
   }

   public void setClient(Client client) {
      this.client = client;
   }

   public boolean isCanceled() {
      return canceled;
   }

   public boolean isFinished() {
      return this.getEndDate().isBefore(LocalDateTime.now());
   }

   public void setCanceled(boolean canceled) {
      this.canceled = canceled;
   }

}