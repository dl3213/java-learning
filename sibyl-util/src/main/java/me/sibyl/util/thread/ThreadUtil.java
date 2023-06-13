package me.sibyl.util.thread;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Classname ThreadUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/20 21:05
 */

public final class ThreadUtil extends cn.hutool.core.thread.ThreadUtil {

    public static <T> CompletableFuture<T> anyOf(CompletableFuture<T>... tasks) {
        return (CompletableFuture<T>) CompletableFuture.anyOf(tasks);
    }

    public static <T> T get(CompletableFuture<T> task) {
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
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
