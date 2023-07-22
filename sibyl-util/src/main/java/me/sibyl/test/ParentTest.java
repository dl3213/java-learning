package me.sibyl.test;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

public class ParentTest {

    @SneakyThrows
    public static void main(String[] args) {
        Son son = new Son();
        //son.test("123"); // x
        //((Parent) son).test("123"); // x
        //Method test = son.getClass().getSuperclass().getMethod("test",String.class); // x
        //test.invoke(((Parent) son),"123"); // x

        ((Parent)Son.class.getSuperclass().newInstance()).test("123");// 1
        ((Parent)son.getClass().getSuperclass().newInstance()).test("123");// 1
    }
}

class Son extends Parent {
    public void test(String test) {
        System.err.println("Son => " + test);
    }
}

class Parent {
    public void test(String test) {
        System.err.println("parent => " + test);
    }
}
