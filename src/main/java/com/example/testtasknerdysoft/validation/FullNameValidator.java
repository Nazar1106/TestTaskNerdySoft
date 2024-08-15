package com.example.testtasknerdysoft.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FullNameValidator implements ConstraintValidator<FullName, String> {

    private static final String FULL_NAME_PATTERN = "^[A-Z][a-z]+ [A-Z][a-z]+$";


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.matches(FULL_NAME_PATTERN);
    }
}
