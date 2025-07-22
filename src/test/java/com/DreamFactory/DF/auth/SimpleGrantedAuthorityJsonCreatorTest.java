package com.DreamFactory.DF.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleGrantedAuthorityJsonCreatorTest {

    @Test
    void should_instantiate_SimpleGrantedAuthorityJsonCreator() throws JsonProcessingException{
        new SimpleGrantedAuthorityJsonCreator("ROLE_ADMIN") {};
    }
}
