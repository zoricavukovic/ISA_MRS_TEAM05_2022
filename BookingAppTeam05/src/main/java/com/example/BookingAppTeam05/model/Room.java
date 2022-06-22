package com.example.BookingAppTeam05.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="rooms", uniqueConstraints={
        @UniqueConstraint(columnNames = {"roomNum", "cottage_id"})})
@SQLDelete(sql
        = "UPDATE rooms "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
public class Room {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="roomNum", nullable = false)
   private int roomNum;

   @Column(name="numOfBeds", nullable = false)
   private int numOfBeds;

   @Column(name = "deleted")
   private boolean deleted;

   public Room() {}

   public Room(int roomNum, int numOfBeds, boolean deleted) {
      this.roomNum = roomNum;
      this.numOfBeds = numOfBeds;
      this.deleted = deleted;
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

   public boolean isDeleted() {
      return deleted;
   }

   public void setDeleted(boolean deleted) {
      this.deleted = deleted;
   }
}