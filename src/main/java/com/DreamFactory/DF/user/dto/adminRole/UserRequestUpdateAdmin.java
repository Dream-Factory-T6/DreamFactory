package com.DreamFactory.DF.user.dto.adminRole;

public record UserRequestUpdateAdmin(
        String username,

        String email,

        String password,

        com.DreamFactory.DF.role.Role role
) {
}
