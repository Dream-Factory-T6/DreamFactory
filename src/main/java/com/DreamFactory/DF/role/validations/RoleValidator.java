package com.DreamFactory.DF.role.validations;

import com.DreamFactory.DF.role.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<ValidRole, Role> {

    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {
        return value != null && Arrays.asList(Role.values()).contains(value);
    }
}
