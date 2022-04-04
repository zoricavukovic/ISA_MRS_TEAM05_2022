/***********************************************************************
 * Module:  Person.java
 * Author:  cr007
 * Purpose: Defines the Class Person
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public abstract class Person {
   private String firstName;
   private String lastName;
   private Date dateOfBirth;
   private String address;
   
   public String getFirstName() {
      return firstName;
   }
   
   /** @param newFirstName */
   public void setFirstName(String newFirstName) {
      firstName = newFirstName;
   }
   
   public String getLastName() {
      return lastName;
   }
   
   /** @param newLastName */
   public void setLastName(String newLastName) {
      lastName = newLastName;
   }
   
   public Date getDateOfBirth() {
      return dateOfBirth;
   }
   
   /** @param newDateOfBirth */
   public void setDateOfBirth(Date newDateOfBirth) {
      dateOfBirth = newDateOfBirth;
   }
   
   public String getAddress() {
      return address;
   }
   
   /** @param newAddress */
   public void setAddress(String newAddress) {
      address = newAddress;
   }

}