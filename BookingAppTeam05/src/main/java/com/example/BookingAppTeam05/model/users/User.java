package com.example.BookingAppTeam05.model.users;

import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.Place;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Inheritance(strategy=TABLE_PER_CLASS)
public abstract class User implements UserDetails {
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

   @JsonIgnore
   @Column(name="password", nullable = false)
   private String password;

   @Column(name="loyaltyPoints")
   private int loyaltyPoints;

   @Column(name="notYetActivated", nullable = false)
   private boolean notYetActivated;

   @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   private Place place;

   @Column(name = "last_password_reset_date")
   private Timestamp lastPasswordResetDate;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name="role_id", nullable = false)
   private Role role;

   @Column(name="deleted", nullable = false)
   private boolean deleted;

   public User() {
   }


   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<Role> roles = new ArrayList<>();
      roles.add(this.role);
      return roles;
   }

   @Override
   public String getUsername() {
      return this.email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }


   public LoyaltyProgram getLoyaltyProgram() {
      if (this.loyaltyPoints < 50)
         return LoyaltyProgram.REGULAR;
      if (this.loyaltyPoints > 50 && this.loyaltyPoints < 100)
         return LoyaltyProgram.SILVER;
      return LoyaltyProgram.GOLD;
   }


   public void setPassword(String password) {
      Timestamp now = new Timestamp(new Date().getTime());
      this.setLastPasswordResetDate(now);
      this.password = password;
   }

   public Long getId() {
      return id;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public LocalDate getDateOfBirth() {
      return dateOfBirth;
   }

   public void setDateOfBirth(LocalDate dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   @Override
   public String getPassword() {
      return password;
   }

   public int getLoyaltyPoints() {
      return loyaltyPoints;
   }

   public void setLoyaltyPoints(int loyaltyPoints) {
      this.loyaltyPoints = loyaltyPoints;
   }

   public boolean isNotYetActivated() {
      return notYetActivated;
   }

   public void setNotYetActivated(boolean notYetActivated) {
      this.notYetActivated = notYetActivated;
   }

   public Place getPlace() {
      return place;
   }

   public void setPlace(Place place) {
      this.place = place;
   }

   public Timestamp getLastPasswordResetDate() {
      return lastPasswordResetDate;
   }

   public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
      this.lastPasswordResetDate = lastPasswordResetDate;
   }

   public Role getRole() {
      return role;
   }

   public void setRole(Role role) {
      this.role = role;
   }

   public boolean isDeleted() {
      return deleted;
   }

   public void setDeleted(boolean deleted) {
      this.deleted = deleted;
   }
}