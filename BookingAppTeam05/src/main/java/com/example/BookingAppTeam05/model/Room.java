/***********************************************************************
 * Module:  Room.java
 * Author:  cr007
 * Purpose: Defines the Class Room
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class Room {
   private int id;
   private int roomNum;
   private int numOfBeds;
   
   public int getRoomNum() {
      return roomNum;
   }
   
   /** @param newRoomNum */
   public void setRoomNum(int newRoomNum) {
      roomNum = newRoomNum;
   }
   
   public int getNumOfBeds() {
      return numOfBeds;
   }
   
   /** @param newNumOfBeds */
   public void setNumOfBeds(int newNumOfBeds) {
      numOfBeds = newNumOfBeds;
   }
   
   public int getId() {
      return id;
   }
   
   /** @param newId */
   public void setId(int newId) {
      id = newId;
   }

}