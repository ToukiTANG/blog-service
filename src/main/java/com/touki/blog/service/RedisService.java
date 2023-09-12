package com.touki.blog.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Touki
 */
public interface RedisService {
    /**
     * 存储hash
     *
     * @param key:   key
     * @param field: field
     * @param value: value
     * @return: void
     */
    void setHash(String key, Object field, Object value);

    /**
     * 判断是否有hashKey
     *
     * @param key:
     * @param field:
     * @return: boolean
     */
    boolean existHashKey(String key, Object field);

    /**
     * hash取值
     *
     * @param key   key
     * @param field field
     * @return Object
     */
    Object getHash(String key, Object field);

    /**
     * 判断是否有key
     *
     * @param key:
     * @return: boolean
     */
    boolean existKey(String key);

    /**
     * string取值
     *
     * @param key: key
     * @return: java.lang.String
     */
    Object getValue(String key);

    /**
     * 储存string
     *
     * @param key   key
     * @param value value
     */
    void setValue(String key, Object value);

    /**
     * hash值自增i
     *
     * @param key   key
     * @param field field
     * @param i     i
     */
    void incrementHash(String key, Object field, long i);

    /**
     * 取hash的map
     *
     * @param key key
     * @return Map<Object, Object>
     */
    Map<Object, Object> getHashMap(String key);

    /**
     * 取hash的value列表
     *
     * @param key      key
     * @param fieldSet keySet
     * @return List<Object>
     */
    List<Object> multiGet(String key, Set<Object> fieldSet);
}
