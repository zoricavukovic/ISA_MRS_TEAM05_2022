package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name= "unavailableDates")
public class UnavailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="dateTime", nullable = false)
    private LocalDateTime dateTime;

    public UnavailableDate() {}

    public UnavailableDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
