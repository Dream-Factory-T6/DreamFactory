package com.DreamFactory.DF.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class RoleDeserializerTest {
    private ObjectMapper objectMapper;

    static class TestDto {
        public Role role;
    }

    @BeforeEach
    void setUo(){
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Role.class, new RoleDeserializer());
        objectMapper.registerModule(simpleModule);
    }

    @Test
    void testDeserializeRole_valid () throws Exception{
    String json = "{\"role\":\"USER\"}";
    TestDto result = objectMapper.readValue(json, TestDto.class);
    assertEquals(Role.USER, result.role);
    }

    @Test
    void testDeserializeRole_invalid () throws Exception{
        String json = "{\"role\":\"INVALID\"}";
        Exception exception = assertThrows(Exception.class, () -> objectMapper.readValue(json, TestDto.class));

        assertTrue(exception.getMessage().contains("Must be USER or ADMIN"));
    }
}
