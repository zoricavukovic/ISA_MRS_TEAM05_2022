package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.User;
import com.example.BookingAppTeam05.model.UserType;

import java.time.LocalDate;

public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String password;
    private int loyaltyPoints;
    private boolean accountAllowed;
    private Place place;
    private UserType userType;
    private LoyaltyProgram loyaltyProgram;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPassword();
        this.loyaltyPoints = user.getLoyaltyPoints();
        this.accountAllowed = user.isAccountAllowed();
        this.userType = user.getUserType();
        this.loyaltyProgram = user.getLoyaltyProgram();
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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }
}
