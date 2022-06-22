package com.example.BookingAppTeam05.dto;

public class PictureDTO {
    private Long id;
    private String picturePath;
    private byte[] data;

    public PictureDTO() {}

    public PictureDTO(String picturePath, byte[] data) {
        this.picturePath = picturePath;
        this.data = data;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
