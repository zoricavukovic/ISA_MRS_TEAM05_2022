package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="navigationEquipments")
public class NavigationEquipment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="name", nullable = false, unique = true)
   private String name;

   public NavigationEquipment() {}

   public NavigationEquipment(String name) {
      this.name = name;
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}