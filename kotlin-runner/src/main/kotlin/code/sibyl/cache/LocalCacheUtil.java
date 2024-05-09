package code.sibyl.cache;

import code.sibyl.repository.SysConfigRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class LocalCacheUtil {

    private final SysConfigRepository sysConfigRepository;

    @Getter
    private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    public Object get(String key) {
        return cache.get(key);
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public void init() {
        log.info("缓存初始化-----start");
        sysConfigRepository.findAll().doOnNext(e -> {
            System.err.println(e);
            this.put(e.getCode(), e.getContent());
        }).subscribe();
        log.info("缓存初始化-----end");
    }
}