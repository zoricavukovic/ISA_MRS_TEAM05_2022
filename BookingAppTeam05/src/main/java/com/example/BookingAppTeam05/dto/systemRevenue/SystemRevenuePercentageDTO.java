package com.example.BookingAppTeam05.dto.systemRevenue;

import com.example.BookingAppTeam05.model.SystemRevenuePercentage;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

public class SystemRevenuePercentageDTO {
    @Min(1)
    @Max(90)
    private Double percentage;
    private LocalDateTime startDate;

    public SystemRevenuePercentageDTO() {}

    public SystemRevenuePercentageDTO(SystemRevenuePercentage systemRevenuePercentage) {
        this.percentage = systemRevenuePercentage.getPercentage();
        this.startDate = systemRevenuePercentage.getStartDate();
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

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
