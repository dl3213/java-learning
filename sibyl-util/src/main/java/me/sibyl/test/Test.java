package me.sibyl.test;

import java.util.Observable;

/**
 * @author dyingleaf3213
 * @Classname Test
 * @Description TODO
 * @Create 2023/03/29 20:44
 */

public class Test {
    public static void main2(String[] args) {
//当前:同步非阻塞
        MyObserverable observerable = new MyObserverable();
        System.err.println("observerable add=>");
        observerable.addObserver(((o, values) -> {
            System.err.println("accept update => " + values);
        }));
        observerable.addObserver(((o, values) -> {
            System.err.println("2accept update => " + values);
        }));
        observerable.addObserver(((o, values) -> {
            System.err.println("3accept update => " + values);
        }));
        System.err.println("notify all");
        observerable.setChanged();//
        observerable.notifyObservers("new world");// -> push
    }

    public static <T> T[] of(T... values){
        return values;
    }

    public static class MyObserverable extends Observable {
        @Override
        public synchronized void setChanged(){
            super.setChanged();
        }
    }
}
