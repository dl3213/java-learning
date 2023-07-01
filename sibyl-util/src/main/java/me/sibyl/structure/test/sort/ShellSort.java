package me.sibyl.structure.test.sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author dyingleaf3213
 * @Classname ShellSort
 * @Description 希尔排序，属于插入排序
 * @Create 2023/06/05 22:35
 */

public class ShellSort {
    public static void main1(String[] args) {
        int[] data = SortUtil.getData_100000();
        long start = System.currentTimeMillis();

        int gap = data.length / 2; // 分组
        int temp = 0, j = 0;
        while (gap > 0) {
            for (int i = 0; i < data.length; i++) { // 局部排序
                temp = data[i];
                j = i - gap;
                while (j >= 0 && temp < data[j]) {
                    data[j + gap] = data[j];
                    j -= gap;
                }
                data[j + gap] = temp;
            }
            gap /= 2;// 每组再分组
        }

        System.err.println(System.currentTimeMillis() - start); // 36
        System.err.println(Arrays.stream(data).boxed().collect(Collectors.toList()));
    }
}
