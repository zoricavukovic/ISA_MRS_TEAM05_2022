package com.example.BookingAppTeam05.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class NewAdditionalServiceDTO {
    @NotBlank
    private String serviceName;

    @Min(1)
    @Max(100000)
    private double price;

    public NewAdditionalServiceDTO() {}

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
