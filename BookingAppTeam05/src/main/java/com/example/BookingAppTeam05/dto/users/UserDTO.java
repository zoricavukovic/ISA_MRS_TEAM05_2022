package com.example.BookingAppTeam05.dto.users;

import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.users.Admin;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.Role;
import com.example.BookingAppTeam05.model.users.User;

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
    private Role userType;
    private int penalties; // only for clients
    private boolean passwordChanged; // only for admins
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
        this.accountAllowed = user.isNotYetActivated();
        this.userType = user.getRole();
        this.loyaltyProgram = user.getLoyaltyProgram();

        setAdditionalFieldIfNeeded(user);
    }


    private void setAdditionalFieldIfNeeded(User user) {
        if (user.getRole().getName().equals("ROLE_CLIENT")) {
            setPenalties(((Client)user).getPenalties());
        } else if (user.getRole().getName().equals("ROLE_ADMIN")) {
            setPasswordChanged(((Admin)user).isPasswordChanged());
        }
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public boolean isAccountAllowed() {
        return accountAllowed;
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

    public Role getUserType() {
        return userType;
    }

    public void setUserType(Role userType) {
        this.userType = userType;
    }

    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }
}
