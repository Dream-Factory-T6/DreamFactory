package com.DreamFactory.DF.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class SimpleGrantedAuthorityJsonCreatorTest {

    @Test
    void should_instantiate_SimpleGrantedAuthorityJsonCreator() throws JsonProcessingException{
        new SimpleGrantedAuthorityJsonCreator("ROLE_ADMIN") {};
    }
}
