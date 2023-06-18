package me.sibyl.util.lock;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname ReentrantLock
 * @Description TODO
 * @Create 2023/06/18 13:04
 */

public class ReentrantLockTest {

    public static void main(String[] args) {
        final Integer[] target = {1};
        ReentrantLock lock = new ReentrantLock();
        Stream.iterate(1, i -> i + 1).limit(4).parallel().forEach(i -> {
            lock.lock();
            try {
                target[0] = target[0] + 1;
            }finally {
                lock.unlock();
            }
        });

        System.err.println("fin target = " + target[0]);
    }
}
