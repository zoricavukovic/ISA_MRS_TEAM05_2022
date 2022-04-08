package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="rooms", uniqueConstraints={
        @UniqueConstraint(columnNames = {"roomNum", "cottage_id"})})
public class Room {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="roomNum", nullable = false)
   private int roomNum;

   @Column(name="numOfBeds", nullable = false)
   private int numOfBeds;

   public Room() {}

   public Room(int roomNum, int numOfBeds) {
      this.roomNum = roomNum;
      this.numOfBeds = numOfBeds;
   }

   public Long getId() {
      return id;
   }

   public int getRoomNum() {
      return roomNum;
   }

   public int getNumOfBeds() {
      return numOfBeds;
   }

   public void setRoomNum(int roomNum) {
      this.roomNum = roomNum;
   }

   public void setNumOfBeds(int numOfBeds) {
      this.numOfBeds = numOfBeds;
   }
}