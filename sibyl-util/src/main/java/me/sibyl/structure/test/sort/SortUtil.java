package me.sibyl.structure.test.sort;

/**
 * @author dyingleaf3213
 * @Classname SortUtil
 * @Description 常见的八种排序算法：插入排序、希尔排序、选择排序、堆排序、冒泡排序、快速排序、归并排序,基数排序
 * @Create 2023/06/05 22:25
 */

public class SortUtil {

    public static void main1(String[] args) {
        System.err.println(Fibonacci(6));
    }

    /**
     * 斐波那契数列 1，1，2，3，5，8，13，21，34，55，89...
     * F[n]=F[n-1]+F[n-2](n>=2,F[0]=1,F[1]=1)
     */
    private static int Fibonacci(int i) {
        if (i <= 2) return 1;
        return Fibonacci(i - 1) + Fibonacci(i - 2);
    }

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
