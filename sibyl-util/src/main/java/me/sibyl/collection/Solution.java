package me.sibyl.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {

    public static void main(String[] args) {
//        int[][] ret = merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}});
//        int[][] ret = merge2(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}});
        int[][] ret = merge3(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}});
        for (int[] ints : ret) {
            System.err.println(ints[0] + "," + ints[1]);
        }
    }

    public static int[][] merge3(int[][] intervals) {
        if (intervals.length == 0) return new int[0][0];
        List<int[]> ans = new ArrayList<>();
        int[] cur;
        sort(intervals, 0, intervals.length - 1);
        cur = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= cur[1]) {
                if (intervals[i][1] > cur[1]) cur[1] = intervals[i][1];
                else continue;
            } else {
                ans.add(cur);
                cur = intervals[i];
            }
        }
        ans.add(cur);

        return ans.toArray(new int[ans.size()][]);
    }

    public static void sort(int[][] intervals, int l, int r) {
        if (l >= r) return;
        int p = partition(intervals, l, r);
        sort(intervals, l, p - 1);
        sort(intervals, p + 1, r);
    }

    public static int partition(int[][] intervals, int l, int r) {
        int[] temp = intervals[l];
        while (l < r) {
            while (l < r) {
                if (intervals[r][0] < temp[0]) {
                    intervals[l] = intervals[r];
                    break;
                }
                r--;
            }
            while (l < r) {
                if (intervals[l][0] > temp[0]) {
                    intervals[r] = intervals[l];
                    break;
                }
                l++;
            }
        }
        intervals[l] = temp;
        return l;
    }

    public static int[][] merge2(int[][] intervals) {
        // 先按照区间起始位置排序
        Arrays.sort(intervals, (v1, v2) -> v1[0] - v2[0]);
        // 遍历区间
        int[][] res = new int[intervals.length][2];
        int idx = -1;
        for (int[] interval : intervals) {
            // 如果结果数组是空的，或者当前区间的起始位置 > 结果数组中最后区间的终止位置，
            // 则不合并，直接将当前区间加入结果数组。
            if (idx == -1 || interval[0] > res[idx][1]) {
                res[++idx] = interval;
            } else {
                // 反之将当前区间合并至结果数组的最后区间
                res[idx][1] = Math.max(res[idx][1], interval[1]);
            }
        }
        return Arrays.copyOf(res, idx + 1);
    }

    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return intervals;
        }
        //先按区间的一个元素排序一遍
        Arrays.sort(intervals, (x, y) -> x[0] - y[0]);
        List<int[]> arr = new ArrayList<int[]>();
        arr.add(intervals[0]);
        for (int i = 1; i < intervals.length; ++i) {
            //[x1,x2]和[y1,y2]比较，如果x2<y1说明这两个区间不想交
            if (arr.get(arr.size() - 1)[1] < intervals[i][0]) {
                arr.add(intervals[i]);
            }
            //否则，将这两个区间合并为 [x1,max(x2,y2)]
            else {
                arr.get(arr.size() - 1)[1] = Math.max(arr.get(arr.size() - 1)[1], intervals[i][1]);
            }
        }
        return arr.toArray(new int[arr.size()][2]);
    }
}
