package com.example.BookingAppTeam05.model;

import com.example.BookingAppTeam05.model.entities.BookingEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="pricelists")
public class Pricelist {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="entityPricePerPerson", nullable = false)
   private double entityPricePerPerson;

   @Column(name="startDate", nullable = false)
   private LocalDateTime startDate;


   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name="price_list_id")
   private Set<AdditionalService> additionalServices;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "booking_entity_id")
   private BookingEntity bookingEntity;

   public Pricelist() {}

   public Pricelist(double entityPricePerPerson, LocalDateTime startDate, Set<AdditionalService> additionalServices) {
      this.entityPricePerPerson = entityPricePerPerson;
      this.startDate = startDate;
      this.additionalServices = additionalServices;
   }

   public BookingEntity getBookingEntity() {
      return bookingEntity;
   }

   public void setBookingEntity(BookingEntity bookingEntity) {
      this.bookingEntity = bookingEntity;
   }

   public Long getId() {
      return id;
   }

   public double getEntityPricePerPerson() {
      return entityPricePerPerson;
   }

   public LocalDateTime getStartDate() {
      return startDate;
   }

   public Set<AdditionalService> getAdditionalServices() {
      return additionalServices;
   }

   public void setEntityPricePerPerson(double entityPricePerPerson) {
      this.entityPricePerPerson = entityPricePerPerson;
   }

   public void setStartDate(LocalDateTime startDate) {
      this.startDate = startDate;
   }

   public void setAdditionalServices(Set<AdditionalService> additionalServices) {
      this.additionalServices = additionalServices;
   }
}