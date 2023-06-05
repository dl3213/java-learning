package me.sibyl.structure.test.sort;

/**
 * @author dyingleaf3213
 * @Classname SortUtil
 * @Description 常见的八种排序算法：插入排序、希尔排序、选择排序、堆排序、冒泡排序、快速排序、归并排序
 * @Create 2023/06/05 22:25
 */

public class SortUtil {

    public static int[] getData_10000() {
        int len = 10000;
        int arr[] = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = len - i;
        }
        return arr;
    }

    public static int[] getData_100000() {
        int len = 100000;
        int arr[] = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = len - i;
        }
        return arr;
    }
}
