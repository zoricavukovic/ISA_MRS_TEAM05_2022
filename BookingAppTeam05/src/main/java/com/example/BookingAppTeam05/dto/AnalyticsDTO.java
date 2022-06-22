package com.example.BookingAppTeam05.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class AnalyticsDTO {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endDate;

    private int numOfReservationPerWeek;
    private int numOfReservationPerMonth;
    private int numOfReservationPerYear;
    private String textValue;
    private double sumCost;

    public AnalyticsDTO() {
    }

    public AnalyticsDTO(LocalDateTime startDate, LocalDateTime endDate, String textValue, int numOfReservationPerWeek,
                        int numOfReservationPerMonth, int numOfReservationPerYear, double sumCost) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.textValue = textValue;
        this.numOfReservationPerWeek = numOfReservationPerWeek;
        this.numOfReservationPerMonth = numOfReservationPerMonth;
        this.numOfReservationPerYear = numOfReservationPerYear;
        this.sumCost = sumCost;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getNumOfReservationPerWeek() {
        return numOfReservationPerWeek;
    }

    public void setNumOfReservationPerWeek(int numOfReservationPerWeek) {
        this.numOfReservationPerWeek = numOfReservationPerWeek;
    }

    public int getNumOfReservationPerMonth() {
        return numOfReservationPerMonth;
    }

    public void setNumOfReservationPerMonth(int numOfReservationPerMonth) {
        this.numOfReservationPerMonth = numOfReservationPerMonth;
    }

    public int getNumOfReservationPerYear() {
        return numOfReservationPerYear;
    }

    public void setNumOfReservationPerYear(int numOfReservationPerYear) {
        this.numOfReservationPerYear = numOfReservationPerYear;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public double getSumCost() {
        return sumCost;
    }

    public void setSumCost(double sumCost) {
        this.sumCost = sumCost;
    }
}
