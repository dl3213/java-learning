package me.sibyl.structure.test;

/**
 * @author dyingleaf3213
 * @Classname Test20220604
 * @Description TODO
 * @Create 2022/06/04 20:23
 */

public class Test20220604 {

    public static void main(String[] args) {

    }

    public static void main2(String[] args) {
        float f = 3.4f;
        double d = 3.4f;
        System.err.println(((Object) f).getClass());
    }

    public static void main1(String[] args) {
        temp:
        for (int i = 0; i < 5; i++) {
            System.err.println(i);
            for (int j = 0; j < 5; j++) {
                if (j == 3) break temp;
            }
        }
    }
}
