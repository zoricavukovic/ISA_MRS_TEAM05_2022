package com.example.BookingAppTeam05.model;

import com.example.BookingAppTeam05.model.users.User;

import javax.persistence.*;

@Entity
@Table(name="deletedAccounts")
public class DeleteAccountRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name="reason", nullable = false, length = 1024)
    private String reason;

    @Column(name="processed", nullable = false)
    private boolean processed;

    @Column(name="accepted", nullable = false)
    private boolean accepted;

    @Column(name="adminResponse", length = 1024)
    private String adminResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    @Column(name="version", unique=false, nullable=false)
    private Long version;

    public DeleteAccountRequest() {}

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public DeleteAccountRequest(Long id, String reason, boolean processed, String adminResponse, User user) {
        this.id = id;
        this.reason = reason;
        this.processed = processed;
        this.adminResponse = adminResponse;
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
