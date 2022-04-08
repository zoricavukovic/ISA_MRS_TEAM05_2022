package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="picture_path", nullable = false, unique = true)
    private String picturePath;

    public Picture() { }

    public Picture(String picturePath) {
        this.picturePath = picturePath;
    }

    public Long getId() {
        return id;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
