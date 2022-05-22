package com.example.BookingAppTeam05.dto;

public class AllRequestsNumsDTO {
    private int reservationReportsCounter;
    private int clientRatingsCounter;
    private int clientComplaintsCounter;
    private int deleteAccountRequests;

    public AllRequestsNumsDTO() {}

    public AllRequestsNumsDTO(int reservationReportsCounter, int clientRatingsCounter, int clientComplaintsCounter, int deleteAccountRequests) {
        this.reservationReportsCounter = reservationReportsCounter;
        this.clientRatingsCounter = clientRatingsCounter;
        this.clientComplaintsCounter = clientComplaintsCounter;
        this.deleteAccountRequests = deleteAccountRequests;
    }

    public int getReservationReportsCounter() {
        return reservationReportsCounter;
    }

    public void setReservationReportsCounter(int reservationReportsCounter) {
        this.reservationReportsCounter = reservationReportsCounter;
    }

    public int getClientRatingsCounter() {
        return clientRatingsCounter;
    }

    public void setClientRatingsCounter(int clientRatingsCounter) {
        this.clientRatingsCounter = clientRatingsCounter;
    }

    public int getClientComplaintsCounter() {
        return clientComplaintsCounter;
    }

    public void setClientComplaintsCounter(int clientComplaintsCounter) {
        this.clientComplaintsCounter = clientComplaintsCounter;
    }

    public int getDeleteAccountRequests() {
        return deleteAccountRequests;
    }

    public void setDeleteAccountRequests(int deleteAccountRequests) {
        this.deleteAccountRequests = deleteAccountRequests;
    }
}

