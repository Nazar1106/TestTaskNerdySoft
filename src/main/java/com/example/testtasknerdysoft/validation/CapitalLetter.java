package com.example.testtasknerdysoft.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CapitalLetterValidator.class)
public @interface CapitalLetter {
    String message() default "Title must start with a capital letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
