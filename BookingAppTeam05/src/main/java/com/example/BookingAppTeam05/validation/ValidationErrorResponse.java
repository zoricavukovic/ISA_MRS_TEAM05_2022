package com.example.BookingAppTeam05.validation;

import java.util.List;
import java.util.ArrayList;

public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    public ValidationErrorResponse() {}

    public List<Violation> getViolations() {
        return violations;
    }

}
