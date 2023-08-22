package com.touki.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Touki
 */
@Slf4j
public abstract class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * json==>object
     *
     * @param content:   json字符串
     * @param valueType: clazz
     * @return: T
     */
    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            log.warn("序列化错误！{}", e.getMessage());
        }
        return null;
    }

    /**
     * stream==>object
     *
     * @param src:       steam流
     * @param valueType: clazz
     * @return: T
     */
    public static <T> T readValue(InputStream src, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(src, valueType);
        } catch (IOException e) {
            log.warn("序列化错误！{}", e.getMessage());
        }
        return null;
    }

    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            log.warn("序列化错误！{}", e.getMessage());
        }
        return null;
    }

    /**
     * object==>json
     *
     * @param value: object对象
     * @return: java.lang.String
     */
    public static String writeValueAsString(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.warn("序列化错误！{}", e.getMessage());
        }
        return null;
    }
}
