package com.example.BookingAppTeam05.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="admins")
public class Admin extends User {
    public Admin() {
    }

    public Admin(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, String password, int loyaltyPoints, boolean accountAllowed, Place place, UserType userType, LoyaltyProgram loyaltyProgram) {
        super(email, firstName, lastName, dateOfBirth, address, phoneNumber, password, loyaltyPoints, accountAllowed, place, userType, loyaltyProgram);
    }
}