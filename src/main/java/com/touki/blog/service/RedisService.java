package com.touki.blog.service;

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

    void incrementHash(String key, Object field, long i);
}
