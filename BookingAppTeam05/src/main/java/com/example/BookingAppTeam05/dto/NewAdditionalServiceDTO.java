package com.example.BookingAppTeam05.dto;

import com.example.BookingAppTeam05.model.AdditionalService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class NewAdditionalServiceDTO {

    private Long id;

    @NotBlank
    private String serviceName;

    @Min(1)
    @Max(100000)
    private double price;

    public NewAdditionalServiceDTO() {}

    public NewAdditionalServiceDTO(Long id, String serviceName, double price) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
    }

    public NewAdditionalServiceDTO(AdditionalService additionalService) {
        this.id = additionalService.getId();
        this.serviceName = additionalService.getServiceName();
        this.price = additionalService.getPrice();
    }

    public Long getId() {
        return id;
    }

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
