/***********************************************************************
 * Module:  User.java
 * Author:  cr007
 * Purpose: Defines the Class User
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public abstract class User {
   private String email;
   private String firstName;
   private String lastName;
   private Date dateOfBirth;
   private String address;
   private String phoneNumber;
   private String password;
   private int loyaltyPoints;
   private boolean accountAllowed;
   
   public Place place;
   public UserType userType;
   public LoyaltyProgram loyaltyProgram;
   
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
   
   public String getPhoneNumber() {
      return phoneNumber;
   }
   
   /** @param newPhoneNumber */
   public void setPhoneNumber(String newPhoneNumber) {
      phoneNumber = newPhoneNumber;
   }
   
   public String getEmail() {
      return email;
   }
   
   /** @param newEmail */
   public void setEmail(String newEmail) {
      email = newEmail;
   }
   
   public String getPassword() {
      return password;
   }
   
   /** @param newPassword */
   public void setPassword(String newPassword) {
      password = newPassword;
   }
   
   public int getLoyaltyPoints() {
      return loyaltyPoints;
   }
   
   /** @param newLoyaltyPoints */
   public void setLoyaltyPoints(int newLoyaltyPoints) {
      loyaltyPoints = newLoyaltyPoints;
   }
   
   public boolean getAccountAllowed() {
      return accountAllowed;
   }
   
   /** @param newAccountAllowed */
   public void setAccountAllowed(boolean newAccountAllowed) {
      accountAllowed = newAccountAllowed;
   }
   
   
   /** @pdGenerated default parent getter */
   public Place getPlace() {
      return place;
   }
   
   /** @pdGenerated default parent setter
     * @param newPlace */
   public void setPlace(Place newPlace) {
      this.place = newPlace;
   }
   /** @pdGenerated default parent getter */
   public UserType getUserType() {
      return userType;
   }
   
   /** @pdGenerated default parent setter
     * @param newUserType */
   public void setUserType(UserType newUserType) {
      this.userType = newUserType;
   }
   /** @pdGenerated default parent getter */
   public LoyaltyProgram getLoyaltyProgram() {
      return loyaltyProgram;
   }
   
   /** @pdGenerated default parent setter
     * @param newLoyaltyProgram */
   public void setLoyaltyProgram(LoyaltyProgram newLoyaltyProgram) {
      this.loyaltyProgram = newLoyaltyProgram;
   }

}