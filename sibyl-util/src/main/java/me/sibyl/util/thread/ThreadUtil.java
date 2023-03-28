package me.sibyl.util.thread;

import java.util.function.Consumer;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void out(Object... args) {
        System.out.printf("[" + Thread.currentThread().getName() + "] ");
        for (Object arg : args) {
            System.out.printf(String.valueOf(arg) + " ");
        }
        System.out.println();
    }

    public static void join() {
        try {
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void join(long millis) {
        try {
            Thread.currentThread().join(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
