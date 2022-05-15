package com.example.BookingAppTeam05.model.users;

import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.Place;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name="admins")
public class Admin extends User {

    @Column(name="passwordChanged", nullable = false)
    private boolean passwordChanged;

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public Admin() {
    }

    public Admin(String email, String firstName, String lastName, String address, String phoneNumber, String password, boolean notYetActivated, Place place, Role role) {
        super(email, firstName, lastName, address, phoneNumber, password, notYetActivated, place, role);
        this.passwordChanged = false;
        this.setDeleted(false);
        this.setLoyaltyPoints(0);
        this.setPlace(place);
    }
}