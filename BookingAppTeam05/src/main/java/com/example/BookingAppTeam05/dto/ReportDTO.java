package com.example.BookingAppTeam05.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReportDTO {
    @NotNull
    private boolean clientCome;
    @Size(max = 250, message = "{validation.name.size.too_long}")
    private String comment;
    @NotNull
    private boolean reward;
    @NotNull
    private Long reservationId;

    public ReportDTO() {
    }

    public ReportDTO(boolean clientCome, String comment, boolean reward, Long reservationId) {
        this.clientCome = clientCome;
        this.comment = comment;
        this.reward = reward;
        this.reservationId = reservationId;
    }

    public boolean isClientCome() {
        return clientCome;
    }

    public void setClientCome(boolean clientCome) {
        this.clientCome = clientCome;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isReward() {
        return reward;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
