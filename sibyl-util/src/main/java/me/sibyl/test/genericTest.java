package me.sibyl.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname genericTest
 * @Description TODO
 * @Create 2023/04/29 20:53
 */

public class genericTest {
    public static void main1(String[] args) {
        //List<? extends Number> list = new ArrayList<Number>();
        List<? super Number> list = new ArrayList<Number>();

        list.add(1);
        System.err.println(list);

        Object object = list.get(0);

        System.err.println(new Object() {
        }.getClass().getEnclosingMethod());

    }
}
