package me.sibyl.util.lock;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname ReentrantLock
 * @Description TODO
 * @Create 2023/06/18 13:04
 */

public class ReentrantLockTest {

    static int target = 0;

    public static ReentrantLock lock = new ReentrantLock();

    @SneakyThrows
    public static void main(String[] args) {

        int times = 100000;
        boolean isLock = true;
        CompletableFuture<Void> add = CompletableFuture.runAsync(() -> {
            for (int j = 0; j < times; j++) {
                if (isLock) {
                    lock.lock();
                    try {
                        target++;
                    } finally {
                        lock.unlock();
                    }

                } else {
                    target++;
                }
            }
        });
        CompletableFuture<Void> sub = CompletableFuture.runAsync(() -> {
            for (int j = 0; j < times; j++) {
                if (isLock) {
                    lock.lock();
                    try {
                        target--;
                    } finally {
                        lock.unlock();
                    }
                } else {
                    target--;
                }
            }
        });
        CompletableFuture.allOf(add, sub).join();
        System.err.println("fin target = " + target);
    }

}
