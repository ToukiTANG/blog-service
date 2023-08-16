package com.touki.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Touki
 */
@Configuration
public class JsonConfig {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        SimpleModule dateModule = new SimpleModule();
        dateModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DATE_FORMAT) {
        }));
        // JSON Long ==> String
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(dateModule);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}

