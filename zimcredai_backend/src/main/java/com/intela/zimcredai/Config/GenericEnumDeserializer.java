package com.intela.zimcredai.Config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;

public class GenericEnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {
    private Class<T> enumClass;

    public GenericEnumDeserializer() {
        // Default constructor for Jackson
    }

    public GenericEnumDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getText();
        if (value == null || value.isEmpty()) {
            throw new JsonMappingException(p, "Enum value cannot be empty");
        }
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new JsonMappingException(p, "Invalid value for enum " + enumClass.getSimpleName() + ": " + value, e);
        }
    }

    public void setEnumClass(Class<T> enumClass) {
        this.enumClass = enumClass;
    }
}
