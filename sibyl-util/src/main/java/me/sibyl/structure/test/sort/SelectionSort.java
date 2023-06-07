package me.sibyl.structure.test.sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author dyingleaf3213
 * @Classname 选择排序
 * @Description TODO
 * @Create 2023/06/06 20:18
 */

public class SelectionSort {
    public static void main(String[] args) {
        int[] data = SortUtil.getData_100000();
        //System.err.println(Arrays.stream(data).boxed().collect(Collectors.toList()));
        long start = System.currentTimeMillis();

        SelectionSort(data);

        System.err.println(System.currentTimeMillis() - start);
        System.err.println(Arrays.stream(data).boxed().collect(Collectors.toList()));
    }

    public static void SelectionSort(int[] arr) {
        if (arr == null || arr.length < 2) return;
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                swap(arr, i, minIndex);
            }
        }
    }

    //交换传递数组下表的i与j元素的交换
    public static void swap(int[] iArray, int i, int j) {
        int tmp = iArray[i];
        iArray[i] = iArray[j];
        iArray[j] = tmp;
    }
}
