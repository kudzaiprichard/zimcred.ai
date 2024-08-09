package com.intela.zimcredai.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.intela.zimcredai.Models.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // Register deserializers for your enums
        module.addDeserializer(Role.class, new GenericEnumDeserializer<>(Role.class));
        // Add other enums if needed

        mapper.registerModule(module);
        return mapper;
    }
}
