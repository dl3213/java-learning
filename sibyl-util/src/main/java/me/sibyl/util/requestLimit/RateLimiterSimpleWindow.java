package me.sibyl.util.requestLimit;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dyingleaf3213
 * @Classname RateLimiterSimpleWindow
 * @Description https://www.jb51.net/article/257674.htm
 * @Create 2023/04/29 22:48
 */
public class RateLimiterSimpleWindow {
    // 阈值
    private static Integer QPS = 2;
    // 时间窗口（毫秒）
    private static long TIME_WINDOWS = 1000;
    // 计数器
    private static AtomicInteger REQ_COUNT = new AtomicInteger();

    private static long START_TIME = System.currentTimeMillis();

    public synchronized static boolean tryAcquire() {
        if ((System.currentTimeMillis() - START_TIME) > TIME_WINDOWS) {
            REQ_COUNT.set(0);
            START_TIME = System.currentTimeMillis();
        }
        return REQ_COUNT.incrementAndGet() <= QPS;
    }

    public static void main2(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (!tryAcquire()) {
                System.out.println(now + " 被限流");
            } else {
                System.out.println(now + " 做点什么");
            }
        }
    }
}