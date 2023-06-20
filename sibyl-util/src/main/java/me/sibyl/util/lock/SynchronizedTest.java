package me.sibyl.util.lock;

import lombok.Data;
import lombok.SneakyThrows;
import me.sibyl.util.thread.ThreadUtil;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname LockTest
 * @Description TODO
 * @Create 2023/06/18 12:33
 */

public class SynchronizedTest {

    public static Object lock = new Object();

    static int target = 0;

    @SneakyThrows
    public static void main(String[] args) {

        int times = 100000;
        boolean isLock = true;
        CompletableFuture<Void> add = CompletableFuture.runAsync(() -> {
            for (int j = 0; j < times; j++) {
                if (isLock) {
                    synchronized (lock) {
                        target++;
                    }
                } else {
                    target++;
                }
            }
        });
        CompletableFuture<Void> sub = CompletableFuture.runAsync(() -> {
            for (int j = 0; j < times; j++) {
                if (isLock) {
                    synchronized (lock) {
                        target--;
                    }
                } else {
                    target--;
                }
            }
        });
        CompletableFuture.allOf(add, sub).join();
        System.err.println("fin target = " + target);
    }


    @Data
    static
    class TestObj {
        private int value = 10;
        private String name = "test";
    }
}
