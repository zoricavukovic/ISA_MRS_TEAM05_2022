package com.example.BookingAppTeam05.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="cottages")
@SQLDelete(sql
        = "UPDATE cottages "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
public class Cottage extends BookingEntity {

   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name = "cottage_id")
   public Set<Room> rooms;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name="cottage_owner_id")
   public CottageOwner cottageOwner;

   @Column(name="deleted")
   private boolean deleted;


   public Cottage() {}

   public Cottage(String promoDescription, Set<Picture> pictures, String address, String name, Set<UnavailableDate> unavailableDates, float entityCancelationRate, EntityType entityType, Set<Pricelist> pricelists, Place place, Set<RuleOfConduct> rulesOfConduct,
                  Set<Client> subscribedClients, Set<Room> rooms, CottageOwner owner) {
      super(promoDescription, pictures, address, name, unavailableDates, entityCancelationRate, entityType, pricelists, place, rulesOfConduct, subscribedClients);
      this.rooms = rooms;
      this.cottageOwner = owner;
   }

   public Set<Room> getRooms() {
      return rooms;
   }

   public CottageOwner getCottageOwner() {
      return cottageOwner;
   }

   public void setRooms(Set<Room> rooms) {
      this.rooms = rooms;
   }

   public void setCottageOwner(CottageOwner owner) {
      this.cottageOwner = owner;
   }

}