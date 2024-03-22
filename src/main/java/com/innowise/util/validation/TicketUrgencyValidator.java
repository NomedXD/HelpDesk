package com.innowise.util.validation;

import com.innowise.domain.enums.TicketUrgency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class TicketUrgencyValidator implements ConstraintValidator<TicketUrgencyValidation, TicketUrgency> {
    private TicketUrgency[] subset;

    @Override
    public void initialize(TicketUrgencyValidation constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(TicketUrgency value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
