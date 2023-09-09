package com.touki.blog.config;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author Touki
 */
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class JsonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // 全局配置序列化返回 JSON 处理
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            // Jackson 序列化 Long 类型超出 JS 最大最小值序列化位字符串转为 String，解决后端返回的类型在前端精度丢失的问题
            javaTimeModule.addSerializer(Long.class, new StringSerializer());

            // Jackson 序列化 BigDecimal 类型为 String，解决后端返回的类型在前端精度丢失的问题
            javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

            // ======================= 时间序列化规则 =======================
            // yyyy-MM-dd HH:mm:ss
            javaTimeModule.addSerializer(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            // yyyy-MM-dd
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
            // HH:mm:ss
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
            // Instant 类型序列化
            javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);


            // ======================= 时间反序列化规则 =======================
            // yyyy-MM-dd HH:mm:ss
            javaTimeModule.addDeserializer(LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            // yyyy-MM-dd
            javaTimeModule.addDeserializer(LocalDate.class,
                    new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
            // HH:mm:ss
            javaTimeModule.addDeserializer(LocalTime.class,
                    new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
            // Instant 反序列化
            javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);

            builder.modules(javaTimeModule);
            // 时区配置
            builder.timeZone(TimeZone.getDefault());
        };
    }
}

