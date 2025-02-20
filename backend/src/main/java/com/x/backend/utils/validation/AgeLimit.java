package com.x.backend.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeLimitValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeLimit {

    String message() default "User does not meet the minimum age requirements";
    int min();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
