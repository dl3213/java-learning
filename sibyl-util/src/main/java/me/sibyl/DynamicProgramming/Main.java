package me.sibyl.DynamicProgramming;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        System.err.println(System.getProperty("java.version"));

//        System.err.println(fib(6)); // version_1

        // fib_version_2_from_top
//        int n = 7;
//        int[] his = new int[n + 1];
//        Arrays.fill(his, -1);
//        System.err.println(fib_version_2_from_top(n, his));
//        System.err.println(fib_version_2_from_top(8));

        //version_3
//        int n = 7;
//        System.err.println(fib_version_3(n));
    }

    public static int fib_version_3(int n) {
        int num_i = 0;
        int num_i_1 = 1;
        int num_i_2 = 1;
        for (int i = 2; i <= n; i++) {
            num_i = num_i_2 + num_i_1;
            num_i_2 = num_i_1;
            num_i_1 = num_i;
        }
        return num_i;
    }


    /**
     * version_2 : 自顶向下的备忘录法（记忆化搜索法）
     */
    private static int fib_version_2_from_top(int n, int[] his) {
//        if (n < 2) {
//            return 1;
//        }
//        // 读取历史
//        if (his[n] != -1) {
//            return his[n];
//        }
//        int result = fib_version_2_from_top(n - 1, his) + fib_version_2_from_top(n - 2, his);
//        // 记录历史
//        his[n] = result;
//        return result;

        return his[n] != -1 ? his[n] : n < 2 ? 1 : fib_version_2_from_top(n - 1, his) + fib_version_2_from_top(n - 2, his);
    }

    public static int fib_version_2_from_top(int n) {
//        int[] dp = new int[n + 1];
//        if (n > 0) dp[1] = 1;
//        for (int i = 2; i <= n; ++i) {
//            dp[i] = dp[i - 1] + dp[i - 2];
//        }

        if (n < 2) return 1;
        int pre = 0, cur = 1;
        for (int i = 2; i <= n; i++) {
            int tmp = cur;
            cur = pre + cur;
            pre = tmp;
        }
        return cur;
    }

    /**
     * version_1
     * 6 = 13
     * 7 = 21
     * 0 ,1 ,2,3,4,5,6,7
     */
    public static int fib(int n) {
        return n < 2 ? 1 : fib(n - 1) + fib(n - 2);
    }
}
