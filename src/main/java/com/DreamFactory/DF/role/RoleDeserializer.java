package com.DreamFactory.DF.role;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            String value = p.getText().toUpperCase();
            try {
                return Role.valueOf(value);
            } catch (IllegalArgumentException e){
                throw new InvalidFormatException(p,"Must be USER or ADMIN", value, Role.class);
            }
        }
}
