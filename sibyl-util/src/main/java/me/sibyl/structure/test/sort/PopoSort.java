package me.sibyl.structure.test.sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author dyingleaf3213
 * @Classname PopoSort
 * @Description 冒泡排序，属于交换排序
 * @Create 2023/06/05 22:09
 */

public class PopoSort {
    public static void main(String[] args) {
        int[] data = SortUtil.getData_100000();
        //System.err.println(Arrays.stream(data).boxed().collect(Collectors.toList()));
        long start = System.currentTimeMillis();
        for (int i = 0; i < data.length - 1; i++) { // 循环查找次最大的元素直到全部排序好
            for (int j = 0; j < data.length - i - 1; j++) { // 求出当前数组最大或最小的那个元素
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
        System.err.println(System.currentTimeMillis() - start);
        System.err.println(Arrays.stream(data).boxed().collect(Collectors.toList()));
    }

}
