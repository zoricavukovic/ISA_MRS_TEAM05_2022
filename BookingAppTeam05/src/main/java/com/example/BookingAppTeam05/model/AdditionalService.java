package com.example.BookingAppTeam05.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="additionalServices", uniqueConstraints={
        @UniqueConstraint(columnNames = {"serviceName", "price_list_id"})})
public class AdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="price", nullable = false)
    private double price;

    @Column(name="serviceName", nullable = false)
    private String serviceName;

    public AdditionalService() { }

    public AdditionalService(double price, String serviceName) {
        this.price = price;
        this.serviceName = serviceName;
    }

    public Long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}

