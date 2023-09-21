package com.touki.blog.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * @author Touki
 */
@Configuration
public class JsonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // 全局配置序列化返回 JSON 处理
            // Jackson 序列化 Long 类型超出 JS 最大最小值序列化位字符串转为 String，解决后端返回的类型在前端精度丢失的问题
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 时区配置
            builder.timeZone(TimeZone.getDefault());
        };
    }
}

