package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="value", nullable = false)
    private float value;

    @Column(name="comment")
    private String comment;

    @Column(name="approved")
    private boolean approved;

    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    public Rating() {}

    public Rating(float value, String comment, boolean approved) {
        this.value = value;
        this.comment = comment;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
