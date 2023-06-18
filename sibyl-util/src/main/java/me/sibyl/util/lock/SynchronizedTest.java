package me.sibyl.util.lock;

import lombok.Data;

import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname LockTest
 * @Description TODO
 * @Create 2023/06/18 12:33
 */

public class SynchronizedTest {

    public static Object object = new Object();

    public static void main(String[] args) {
        final Integer[] target = {1};
        Stream.iterate(1, i -> i + 1).limit(4).parallel().forEach(i -> {
            synchronized (object){
                target[0] = target[0] + 1;
            }
        });

        System.err.println("fin target = " + target[0]);
    }


    @Data
    static
    class TestObj {
        private int value = 10;
        private String name = "test";
    }
}
