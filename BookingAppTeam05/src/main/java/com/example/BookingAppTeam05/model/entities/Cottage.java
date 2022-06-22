package com.example.BookingAppTeam05.model.entities;

import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="cottages")
public class Cottage extends BookingEntity {

   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name = "cottage_id")
   private Set<Room> rooms = new HashSet<>();

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name="cottage_owner_id")
   private CottageOwner cottageOwner;


   public Cottage() {}

   public Cottage(String promoDescription, Set<Picture> pictures, String address, String name, Set<UnavailableDate> unavailableDates, float entityCancelationRate, EntityType entityType, Set<Pricelist> pricelists, Place place, Set<RuleOfConduct> rulesOfConduct,
                  Set<Client> subscribedClients, Set<Room> rooms, CottageOwner owner) {
      super(promoDescription, pictures, address, name, unavailableDates, entityCancelationRate, entityType, pricelists, place, rulesOfConduct, subscribedClients);
      this.rooms = rooms;
      this.cottageOwner = owner;
   }
   public Cottage(String promoDescription, String address, String name, float entityCancelationRate) {
      super(promoDescription, address, name, entityCancelationRate, EntityType.COTTAGE);
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