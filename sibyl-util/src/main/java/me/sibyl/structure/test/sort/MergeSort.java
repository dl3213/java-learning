package me.sibyl.structure.test.sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author dyingleaf3213
 * @Classname MergeSort
 * @Description 归并排序
 * @Create 2023/06/05 22:18
 */

public class MergeSort {

    public static void main1(String[] args) {
        int len = 100000;
        int arr[] = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = len - i;
        }
        System.err.println(Arrays.stream(arr).boxed().collect(Collectors.toList()));
        long start = System.currentTimeMillis();
        sortArray(arr);
        System.err.println(System.currentTimeMillis() - start); // 36
        System.err.println(Arrays.stream(arr).boxed().collect(Collectors.toList()));
    }

    private static int[] sortArray(int[] nums) {
        int len = nums.length;
        int[] temp = new int[len];
        mergeSort(nums, 0, len - 1, temp);
        return nums;
    }

    private static void mergeSort(int[] nums, int left, int right, int[] temp) {
        if (left == right) {//当拆分到数组当中只要一个值的时候，结束递归
            return;
        }
        int mid = (left + right) / 2;   //找到下次要拆分的中间值
        mergeSort(nums, left, mid, temp);//记录树左边的
        mergeSort(nums, mid + 1, right, temp);//记录树右边的

        //合并两个区间
        for (int i = left; i <= right; i++) {
            temp[i] = nums[i];
//temp就是辅助列表，新列表的需要排序的值就是从辅助列表中拿到的
        }
        int i = left;       //给辅助数组里面的值标点
        int j = mid + 1;
        for (int k = left; k <= right; k++) {//k 就为当前要插入的位置
            if (i == mid + 1) {
                nums[k] = temp[j];
                j++;
            } else if (j == right + 1) {
                nums[k] = temp[i];
                i++;
            } else if (temp[i] <= temp[j]) {
                nums[k] = temp[i];
                i++;
            } else {
                nums[k] = temp[j];
                j++;
            }
        }
    }
}
