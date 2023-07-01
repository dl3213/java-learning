package me.sibyl.util.lock;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;
import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname ReentrantLock
 * @Description TODO
 * @Create 2023/06/18 13:04
 */


public class ReentrantLockTest {

    static int target = 0;
    public static StampedLock stampedLock = new StampedLock();
    public static ReentrantLock lock = new ReentrantLock();

    @SneakyThrows
    public static void main1(String[] args) {

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

    class MyLock implements Lock{

        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
    class MySync extends AbstractQueuedSynchronizer{

    }
}
