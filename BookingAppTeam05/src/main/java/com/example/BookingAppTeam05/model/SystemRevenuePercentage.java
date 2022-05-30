package com.example.BookingAppTeam05.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="systemRevenuePercentages")
public class SystemRevenuePercentage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="percentage", nullable = false)
    private Double percentage;

    @Column(name="startDate", nullable = false)
    private LocalDateTime startDate;

    public SystemRevenuePercentage() {}

    public SystemRevenuePercentage(Double percentage, LocalDateTime startDate) {
        this.percentage = percentage;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
}
