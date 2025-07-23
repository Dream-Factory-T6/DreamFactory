package com.DreamFactory.DF.user.dto;

import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.role.RoleDeserializer;
import com.DreamFactory.DF.role.validations.ValidRole;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestAdmin(
        @NotBlank(message = "Username cannot be blank") @Size(max = 50, message = "Username cannot be longer than 50 characters!")
        String username,

        @NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid!")
        String email,

        @NotBlank(message = "Password cannot be blank") @Size(min = 8, message = "Password should be at least 8 characters long!")
        String password,

        @JsonDeserialize(using = RoleDeserializer.class)
        @NotNull(message = "Role cannot be null")
        @ValidRole
        Role role
) {
}
