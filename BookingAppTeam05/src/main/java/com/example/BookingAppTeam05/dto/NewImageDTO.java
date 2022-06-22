package com.example.BookingAppTeam05.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class NewImageDTO {
    @NotBlank
    private String imageName;

    @NotBlank
    private String dataBase64;

    public NewImageDTO() {

    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDataBase64() {
        return dataBase64;
    }

    public void setDataBase64(String dataBase64) {
        this.dataBase64 = dataBase64;
    }
}
