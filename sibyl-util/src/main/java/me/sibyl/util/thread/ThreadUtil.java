package me.sibyl.util.thread;

/**
 * @Classname ThreadUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/20 21:05
 */

public final class ThreadUtil {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
