package com.example.BookingAppTeam05.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewRuleOfConductDTO {
    @NotBlank
    private String ruleName;

    @NotNull
    private Boolean allowed;

    public NewRuleOfConductDTO() {}

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }
}
