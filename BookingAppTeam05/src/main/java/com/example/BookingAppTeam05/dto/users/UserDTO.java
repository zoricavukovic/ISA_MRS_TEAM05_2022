package com.example.BookingAppTeam05.dto.users;

import com.example.BookingAppTeam05.model.LoyaltyProgramEnum;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.users.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class UserDTO {
    private Long id;
    @NotBlank
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String email;
    @NotBlank
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String firstName;
    @NotBlank
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String lastName;

    private LocalDate dateOfBirth;
    @NotBlank
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String address;
    @NotBlank(message = "mobileNumber is required")
    @Size(min = 7, max = 10)
    private String phoneNumber;
    @NotBlank
    @Size(max = 1024, message = "{validation.name.size.too_long}")
    private String password;
    private int loyaltyPoints;
    private boolean accountAllowed;
    private Place place;
    private Role userType;
    private String userTypeValue;
    private int penalties; // only for clients
    private boolean captain; // only for shipOwners
    private boolean passwordChanged; // only for admins
    private String reason; //only for owners
    private LoyaltyProgramEnum loyaltyProgram;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPassword();
        this.loyaltyPoints = user.getLoyaltyPoints();
        this.accountAllowed = user.isNotYetActivated();
        this.userType = user.getRole();
        this.loyaltyProgram = user.getLoyaltyProgram();
        setAdditionalFieldIfNeeded(user);
    }


    private void setAdditionalFieldIfNeeded(User user) {
        switch (user.getRole().getName()) {
            case "ROLE_CLIENT":
                setPenalties(((Client) user).getPenalties());
                break;
            case "ROLE_ADMIN":
                setPasswordChanged(((Admin) user).isPasswordChanged());
                break;
            case "ROLE_SHIP_OWNER":
                setCaptain(((ShipOwner) user).isCaptain());
                setReason(((ShipOwner) user).getReason());
                break;
            case "ROLE_COTTAGE_OWNER":
                setReason(((CottageOwner) user).getReason());
                break;
            case "ROLE_INSTRUCTOR":
                setReason(((Instructor) user).getReason());
                break;
        }
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public boolean isAccountAllowed() {
        return accountAllowed;
    }

    public void setAccountAllowed(boolean accountAllowed) {
        this.accountAllowed = accountAllowed;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Role getUserType() {
        return userType;
    }

    public void setUserType(Role userType) {
        this.userType = userType;
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }

    public LoyaltyProgramEnum getLoyaltyProgram() {
        return loyaltyProgram;
    }

    public void setLoyaltyProgram(LoyaltyProgramEnum loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }

    public String getUserTypeValue() {
        return userTypeValue;
    }

    public void setUserTypeValue(String userTypeValue) {
        this.userTypeValue = userTypeValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
