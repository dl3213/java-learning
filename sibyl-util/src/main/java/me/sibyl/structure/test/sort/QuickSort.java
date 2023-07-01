package me.sibyl.structure.test.sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author dyingleaf3213
 * @Classname QuickSort
 * @Description 快速排序
 * @Create 2023/06/05 22:09
 */

public class QuickSort {
    public static void main1(String[] args) {
        int arr[] = SortUtil.getData_10000();
        long start = System.currentTimeMillis();
        QuickSort(arr, 0, arr.length - 1);
        System.err.println(System.currentTimeMillis() - start);
        System.err.println(Arrays.stream(arr).boxed().collect(Collectors.toList()));
    }


    // v2
    public static void QuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotpos = partition(arr, low, high);
            QuickSort(arr, low, pivotpos - 1);
            QuickSort(arr, pivotpos + 1, high);
        }
    }
    // v2
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[low];
        while (low < high) {
            //起初，一定要从右边指针开始，因为arr[low]的值已经扔给了pivot，arr[low]
            //想象成无数字的空位
            while (low < high && pivot <= arr[high]) {
                --high;
            }

            //把比pivot的小的数扔到左边指针
            //把arr[high]扔到arr[low]这个空位上
            //然后，high位置可以想象成无数字的空位
            arr[low] = arr[high];

            while (low < high && arr[low] <= pivot) {
                ++low;
            }
            //把比pivot大的数扔到右边
            //把arr[low]扔到arr[high]这个空位上
            //然后，low位置可以想象成是无数字的空位
            arr[high] = arr[low];
        }
        //此时low==high,return high也一样
        arr[low] = pivot;
        return low;
    }

    //快排实现方法 v1
    public static void quickRow(int[] array, int low, int high) {
        int i, j, pivot;
        //结束条件
        if (low >= high) {
            return;
        }
        i = low;
        j = high;
        //选择的节点，这里选择的数组的第一数作为节点
        pivot = array[low];
        while (i < j) {
            //从右往左找比节点小的数，循环结束要么找到了，要么i=j
            while (array[j] >= pivot && i < j) {
                j--;
            }
            //从左往右找比节点大的数，循环结束要么找到了，要么i=j
            while (array[i] <= pivot && i < j) {
                i++;
            }
            //如果i!=j说明都找到了，就交换这两个数
            if (i > j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        //i==j一轮循环结束，交换节点的数和相遇点的数
        array[low] = array[i];
        array[i] = pivot;
        //数组“分两半”,再重复上面的操作
        quickRow(array, low, i - 1);
        quickRow(array, i + 1, high);
    }
}
