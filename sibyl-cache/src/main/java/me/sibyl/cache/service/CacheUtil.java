package me.sibyl.cache.service;

/**
 * @author dyingleaf3213
 * @Classname CacheUtil
 * @Description TODO
 * @Create 2023/03/26 04:12
 */

public interface CacheUtil {

    Object get(Object key);

    void put(Object key, Object value);
}
