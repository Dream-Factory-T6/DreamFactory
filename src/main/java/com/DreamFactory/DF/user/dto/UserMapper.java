package com.DreamFactory.DF.user.dto;

import com.DreamFactory.DF.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserResponse fromEntity(User user){
        if (user == null){
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public static User toEntity(UserRequest userRequest){
        if (userRequest == null){
            return null;
        }

        User user = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .build();

        return user;
    }

    public static User toEntityRole(UserRequestAdmin userRequest){
        if (userRequest == null){
            return null;
        }

        User user = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .build();

        return user;
    }
}

