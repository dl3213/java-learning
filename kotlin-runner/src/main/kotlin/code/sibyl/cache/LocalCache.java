package code.sibyl.cache;

import code.sibyl.common.r;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
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

    static class LargeObject {
        // 每个对象占用一个较大的内存块
        private byte[] data = new byte[1024 * 1024]; // 1 MB
    }

    /**
     * 1. 生成./hprof文件 1)-Xms32m -Xmx32m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\4code\4java\workspace
     *      2)或者jmap -heap [pid]  or jmap.exe -dump:live,file=./ [pid]
     * 2.启动 visualvm，拖进./hprof文件
     */
    public void test() {
        // 创建一个列表来存储对象引用
        List<LargeObject> objects = new ArrayList<>();

        try {
            while (true) {
                // 创建新的 LargeObject 实例并添加到列表中
                objects.add(new LargeObject());
                // 打印当前对象数量
                System.out.println("Number of objects: " + objects.size());
                // 可以添加一些延迟来控制内存消耗速度
                 r.sleep(100); // 注释掉，或根据需要调整
            }
        } catch (OutOfMemoryError e) {
            System.err.println("OutOfMemoryError caught: " + e.getMessage());
            e.printStackTrace();
        }
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