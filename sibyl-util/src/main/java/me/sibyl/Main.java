package me.sibyl;

public class Main {
    public static void main(String[] args) {
        System.err.println("main");
        System.err.println();

//        System.err.println(Integer.toBinaryString(2));
//        System.err.println(Integer.toBinaryString(1));
//        System.err.println(Integer.toBinaryString(4));
//        System.err.println(Integer.parseInt("100",2));
//        System.err.println();
//
//        System.err.println(2 << 1);// 10 << 1 = 100 << 表示左移移，不分正负数，低位补0　
//        System.err.println(2 >> 1);// 10 >> 1 = 01 >> 表示右移，如果该数为正，则高位补0，若为负数，则高位补1；
//        System.err.println(2 >>> 1);// 10 >> 1 = 01  >>>  表示无符号右移，也叫逻辑右移，即若该数为正，则高位补0，而若该数为负数，则右移后高位同样补0　
//        System.err.println();
//
//        System.err.println(~ 0);
//        System.err.println(~ -1);
//        System.err.println(~ 1);
//        System.err.println();


        System.err.println(Integer.toBinaryString(2));
        System.err.println(Integer.parseInt("1111",2));
        System.err.println(2 ^ 15);
        System.err.println(Integer.toBinaryString(2 ^ 15));
    }
}
