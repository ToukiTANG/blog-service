package com.touki.blog.service.impl;

import com.touki.blog.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Touki
 */
@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 存储hash
     *
     * @param key   :   key
     * @param field : field
     * @param value : value
     * @return: void
     */
    @Override
    public void setHash(String key, Object field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 判断是否有hashKey
     *
     * @param key   :
     * @param field :
     * @return: boolean
     */
    @Override
    public boolean existHashKey(String key, Object field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * hash取值
     *
     * @param key   key
     * @param field field
     * @return Object
     */
    @Override
    public Object getHash(String key, Object field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 判断是否有key
     *
     * @param key :
     * @return: boolean
     */
    @Override
    public boolean existKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * string取值
     *
     * @param key : key
     * @return: java.lang.String
     */
    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 存储string
     *
     * @param key   :   key
     * @param value : jsonString
     * @return: void
     */
    @Override
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
