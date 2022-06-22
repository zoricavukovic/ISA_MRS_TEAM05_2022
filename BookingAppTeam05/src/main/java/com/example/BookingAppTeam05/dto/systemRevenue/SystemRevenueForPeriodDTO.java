package com.example.BookingAppTeam05.dto.systemRevenue;

import javax.validation.constraints.NotNull;

public class SystemRevenueForPeriodDTO {
    private Double total;
    private Double shipsRevenue;
    private Double adventuresRevenue;
    private Double cottagesRevenue;

    public SystemRevenueForPeriodDTO() {}

    public SystemRevenueForPeriodDTO(Double total, Double shipsRevenue, Double adventuresRevenue, Double cottagesRevenue) {
        this.total = total;
        this.shipsRevenue = shipsRevenue;
        this.adventuresRevenue = adventuresRevenue;
        this.cottagesRevenue = cottagesRevenue;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getShipsRevenue() {
        return shipsRevenue;
    }

    public void setShipsRevenue(Double shipsRevenue) {
        this.shipsRevenue = shipsRevenue;
    }

    public Double getAdventuresRevenue() {
        return adventuresRevenue;
    }

    public void setAdventuresRevenue(Double adventuresRevenue) {
        this.adventuresRevenue = adventuresRevenue;
    }

    public Double getCottagesRevenue() {
        return cottagesRevenue;
    }

    public void setCottagesRevenue(Double cottagesRevenue) {
        this.cottagesRevenue = cottagesRevenue;
    }
}
