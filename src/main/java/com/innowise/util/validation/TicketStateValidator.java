package com.innowise.util.validation;

import com.innowise.domain.enums.TicketState;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class TicketStateValidator implements ConstraintValidator<TicketStateValidation, TicketState> {
    private TicketState[] subset;

    @Override
    public void initialize(TicketStateValidation constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(TicketState value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
