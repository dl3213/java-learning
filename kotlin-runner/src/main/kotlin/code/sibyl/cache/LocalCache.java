package code.sibyl.cache;

import code.sibyl.common.r;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LocalCache {

    private LocalCache() {
        this.cacheMap = new HashMap<>();
    }

    private static LocalCache instance = new LocalCache();

    public static LocalCache getInstance() {
        return instance;
    }

    public static LocalCache getBean() {
        return r.getBean(LocalCache.class);
    }

    @Getter
    private Map<String, Object> cacheMap;

    public Object get(String key) {
        return cacheMap.get(key);
    }

    public void put(String key, Object value) {
        cacheMap.put(key, value);
    }

}