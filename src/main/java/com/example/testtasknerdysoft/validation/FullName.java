package com.example.testtasknerdysoft.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FullNameValidator.class)
public @interface FullName {
    String message() default "Author should contain two capital words with name and surname and space between";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
