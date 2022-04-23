package com.example.BookingAppTeam05.model.users;

import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.Place;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name="admins")
@SQLDelete(sql = "UPDATE admins SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Admin extends User {
    public Admin() {
    }
}