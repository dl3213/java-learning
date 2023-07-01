package me.sibyl.structure.test.sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author dyingleaf3213
 * @Classname InsertionSort
 * @Description 插入排序，属于插入排序，时间=O（n²），空间=O(1)
 * @Create 2023/06/05 22:25
 */

public class InsertionSort {

    public static void main1(String[] args) {
        int[] data = SortUtil.getData_100000();
        long start = System.currentTimeMillis();
        int length = data.length;
        for (int i = 0; i < length; i++) {
            // 从第一位开始
            for (int j = i; j > 0; j--) { // 这个遍历是前面(比较过的数组)与后面(未比较过的位数)做比较
                if (data[j - 1] > data[j]) { //小的就放前，大的放后
                    // 交换
                    int temp = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = temp;
                }
            }
        }
        System.err.println(System.currentTimeMillis() - start);
        System.err.println(Arrays.stream(data).boxed().collect(Collectors.toList()));
    }
}
