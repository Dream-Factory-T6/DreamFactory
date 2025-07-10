package com.DreamFactory.DF.user.dto;

public record UserResponse(
        Long id,
        String username,
        String email
) {
}
