package com.DreamFactory.DF.role;

import com.DreamFactory.DF.role.validations.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class RoleValidatorTest {

    private RoleValidator roleValidator;

    @BeforeEach
    void setUp() {
        roleValidator = new RoleValidator();
    }

    @Test
    void testValidRole_USER() {
        assertTrue(roleValidator.isValid(Role.USER, null));
    }

    @Test
    void testValidRole_ADMIN() {
        assertTrue(roleValidator.isValid(Role.ADMIN, null));
    }

    @Test
    void testInvalidRole_null(){
        assertFalse(roleValidator.isValid(null, null));
    }

    @Test
    void testInvalidRole_USRE() {
        assertThrows(IllegalArgumentException.class, () -> Role.valueOf("USRE"));
    }

}
