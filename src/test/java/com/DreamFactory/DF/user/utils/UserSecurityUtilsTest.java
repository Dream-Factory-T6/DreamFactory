package com.DreamFactory.DF.user.utils;

import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class UserSecurityUtilsTest {

    @Test
    void when_createUserByUserDetails_return_createsValidUserDetails() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails result = UserSecurityUtils.createUserByUserDetails(user, authorities);

        assertEquals("testUser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertTrue(result.isEnabled());
        assertEquals(new HashSet<>(authorities), new HashSet<>(result.getAuthorities()));
    }

    @Test
    void getAuthoritiesRole() {
        User user = new User();
        Set<Role> roles = Set.of(Role.USER, Role.ADMIN);
        user.setRoles(roles);

        List<GrantedAuthority> result = UserSecurityUtils.getAuthoritiesRole(user);

        List<String> authorities = result.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertEquals(2, authorities.size());
    }
}