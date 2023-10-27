package com.touki.blog.service.impl;

import com.touki.blog.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Touki
 */
@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
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
     * @param key: key
     * @return Object
     */
    @Override
    public Object getValue(String key) {
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
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void incrementHash(String key, Object field, long i) {
        redisTemplate.opsForHash().increment(key, field, i);
    }

    @Override
    public Map<Object, Object> getHashMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public List<Object> multiGet(String key, Set<Object> fieldSet) {
        return redisTemplate.opsForHash().multiGet(key, fieldSet);
    }

    @Override
    public void removeKey(String[] key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    @Override
    public void removeKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void removeKeyPattern(String keyPattern) {
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public boolean hasValueInSet(String key, String value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    @Override
    public void saveValueToSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }
}
