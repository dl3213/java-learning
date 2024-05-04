package code.sibyl.cache;

import java.util.concurrent.ConcurrentHashMap;

public class CacheMap {
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    public Object getFromCache(String key) {
        return cache.get(key);
    }

    public void addToCache(String key, Object value) {
        cache.put(key, value);
    }

    public static void main(String[] args) {
        CacheMap cacheExample = new CacheMap();

        // 添加数据到缓存
        cacheExample.addToCache("exampleKey", "exampleValue");

        // 从缓存中获取数据
        Object value = cacheExample.getFromCache("exampleKey");
        System.out.println(value); // 输出: exampleValue
    }
}