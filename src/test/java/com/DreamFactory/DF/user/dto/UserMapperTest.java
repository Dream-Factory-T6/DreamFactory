package com.DreamFactory.DF.user.dto;

import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class UserMapperTest {

    @Test
    void when_fromEntityUserIsNull_return_null() {
        UserResponse response = UserMapper.fromEntity(null);
        assertNull(response);
    }

    @Test
    void when_fromEntityNoRoles_return_emptyRoles(){
        User user = new User();
        user.setId(1L);
        user.setEmail("testUser@email.com");
        user.setUsername("testUser");
        user.setRoles(null);

        UserResponse response = UserMapper.fromEntity(user);

        assertNotNull(response);
        assertNotNull(response.roles());
        assertTrue(response.roles().isEmpty(), "Expected roles to be empty when user has no roles");

    }

    @Test
    void when_toEntityUserIsNull_return_null() {
        User response = UserMapper.toEntity(null);
        assertNull(response);
    }

    @Test
    void when_fromEntityAdminIsNull_return_null() {
        User response = UserMapper.toEntityAdmin(null);
        assertNull(response);
    }
}
