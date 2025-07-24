package com.DreamFactory.DF.user.dto;

import com.DreamFactory.DF.user.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static UserResponse fromEntity(User user) {
        if (user == null) {
            return null;
        }

        java.util.Set<String> roles = user.getRoles() == null ? java.util.Collections.emptySet()
                : user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(java.util.stream.Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        );
    }

    public static User toEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }

        User user = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .build();

        return user;
    }

    public static User toEntityAdmin(UserRequestAdmin userRequest) {
        if (userRequest == null) {
            return null;
        }

        User user = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .build();

        return user;
    }
}

