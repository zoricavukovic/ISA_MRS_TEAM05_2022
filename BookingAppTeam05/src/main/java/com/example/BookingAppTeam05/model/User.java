package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDate;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Inheritance(strategy=TABLE_PER_CLASS)
public abstract class User {
   @Id
   @SequenceGenerator(name = "generator1", sequenceName = "usersIdGen", initialValue = 1, allocationSize = 1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator1")
   private Long id;

   @Column(name="email", nullable = false, unique = true)
   private String email;

   @Column(name="firstName", nullable = false)
   private String firstName;

   @Column(name="lastName", nullable = false)
   private String lastName;

   @Column(name="dateOfBirth")
   private LocalDate dateOfBirth;

   @Column(name="address", nullable = false)
   private String address;

   @Column(name="phoneNumber", nullable = false)
   private String phoneNumber;

   @Column(name="password", nullable = false)
   private String password;

   @Column(name="loyaltyPoints")
   private int loyaltyPoints;

   @Column(name="accountAllowed", nullable = false)
   private boolean accountAllowed;

   @ManyToOne(fetch = FetchType.LAZY)
   public Place place;

   @Column(name="userType", nullable = false)
   @Enumerated(EnumType.STRING)
   public UserType userType;

   @Enumerated(EnumType.STRING)
   public LoyaltyProgram loyaltyProgram;


   public User(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, String password, int loyaltyPoints, boolean accountAllowed, Place place, UserType userType, LoyaltyProgram loyaltyProgram) {
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.dateOfBirth = dateOfBirth;
      this.address = address;
      this.phoneNumber = phoneNumber;
      this.password = password;
      this.loyaltyPoints = loyaltyPoints;
      this.accountAllowed = accountAllowed;
      this.place = place;
      this.userType = userType;
      this.loyaltyProgram = loyaltyProgram;
   }

   public User() {

   }

   public Long getId() {
      return id;
   }

   public String getEmail() {
      return email;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public LocalDate getDateOfBirth() {
      return dateOfBirth;
   }

   public String getAddress() {
      return address;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public String getPassword() {
      return password;
   }

   public int getLoyaltyPoints() {
      return loyaltyPoints;
   }

   public boolean isAccountAllowed() {
      return accountAllowed;
   }

   public Place getPlace() {
      return place;
   }

   public UserType getUserType() {
      return userType;
   }

   public LoyaltyProgram getLoyaltyProgram() {
      if (this.loyaltyPoints < 50)
         return LoyaltyProgram.REGULAR;
      if (this.loyaltyPoints > 50 && this.loyaltyPoints < 100)
         return LoyaltyProgram.SILVER;
      return LoyaltyProgram.GOLD;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public void setDateOfBirth(LocalDate dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setLoyaltyPoints(int loyaltyPoints) {
      this.loyaltyPoints = loyaltyPoints;
   }

   public void setAccountAllowed(boolean accountAllowed) {
      this.accountAllowed = accountAllowed;
   }

   public void setPlace(Place place) {
      this.place = place;
   }

   public void setUserType(UserType userType) {
      this.userType = userType;
   }

   public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
      this.loyaltyProgram = loyaltyProgram;
   }
}