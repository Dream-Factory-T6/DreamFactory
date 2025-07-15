package com.DreamFactory.DF.role.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    String message() default "Invalid role, must be USER or ADMIN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
