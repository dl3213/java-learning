package me.sibyl.test;

public class LongestCommonSubstr {

    /**
     * https://blog.csdn.net/qq_40241957/article/details/124289935
     */
    public static void main(String[] args) {
        String s1 = "123456789";
        String s2 = "875456321";

        System.err.println(二维数组比较(s1, s2));
        System.err.println(findCommonSubStr(s1, s2));
        System.err.println(findCommonSubStrWithWindow(s1, s2));

    }

    private static String 二维数组比较(String s1, String s2) {
        int n = s2.length();
        int m = s1.length();
        int col = m - 1;//出发点的列
        int row = 0;//出发点的行
        int max = 0;//最大长度
        int pos = -1;//因为我们的最长公共子串是从s2中截取的，所以这里用pos来标识s2中公共子串的结束位置索引
        while (col != 0 || row < n) {
            //初始化 i,j
            int i = row;
            int j = col;
            int len = 0;
            while (i < n && j < m) {//划“捺”，这里的while()里面的限制就是为了不让划“捺”划到超出整个棋盘
                if (s1.charAt(j) == s2.charAt(i)) {
                    len++;
                    if (len > max) {
                        max = len;
                        pos = i;
                    }

                } else {
                    len = 0;
                }
                i++;
                j++;
            }
            if (col == 0) {//如果出发点已经移到了最左边了，那就只能向下方移动了
                row++;
            } else { //如果没有移到最左边，那就继续往左边移动
                col--;
            }
        }
        String str = s2.substring(pos - max + 1, pos + 1);
        if (str.length() == 0) {
            str = "-1";
        }
        return str;
    }

    public static String findCommonSubStr(String str1, String str2) {
        int str1lenth = str1.length(), str2lenth = str2.length();
        int[][] dp = new int[str1lenth + 1][str2lenth + 1];
        int maxLength = 0; //公共子串的最大长度
        int pos = -1;//因为我们的最长公共子串是从s2中截取的，所以这里用pos来标识s2中公共子串的结束位置索引
        for (int i = str1lenth - 1; i >= 0; i--) {
            for (int j = str2lenth - 1; j >= 0; j--) {
                dp[i][j] = str1.charAt(i) == str2.charAt(j) ? dp[i + 1][j + 1] + 1 : 0;
                if (dp[i][j] > maxLength) {
                    maxLength = dp[i][j];
                    pos = j;
                }
            }
        }
        return str2.substring(pos, pos + maxLength);
    }

    public static String findCommonSubStrWithWindow(String A, String B) {
        //一定要先明确A和B哪个长哪个短
        return A.length() < B.length() ? findMax(A, B) : findMax(B, A);
    }

    //A串的长度较小， B串的长度较大
    public static String findMax(String A, String B) {
        String result = "";
        String maxStr = "";
        //B的火车头进入A轨道，在轨道里面一直往右边移动 但火车头没有离开A轨道
        for (int len = 1; len <= A.length(); len++) {
            maxStr = maxStr(A, 0, B, B.length() - len, len);
            if (maxStr.length() > result.length()) {
                result = maxStr;
            }
        }
        // B火车头跑出出了A轨道，火车尾还没有进入A轨道
        for (int j = B.length() - A.length(); j >= 0; j--) {
            maxStr = maxStr(A, 0, B, j, A.length());
            if (maxStr.length() > result.length()) {
                result = maxStr;
            }
        }
        //B火车尾进入A轨道，继续往前开，但是火车尾还没有离开A轨道
        for (int i = 1; i < A.length(); i++) {
            maxStr = maxStr(A, i, B, 0, A.length() - i);
            if (maxStr.length() > result.length()) {
                result = maxStr;
            }
        }
        return result;
    }

    //A子串从i位置和Bzi子串的j位置 对齐，然后开始从头一一比对各个字符是否相同
    // len参数是指比对的最大长度
    public static String maxStr(String A, int i, String B, int j, int len) {
        int count = 0, max = 0;
        int pos = 0;
        for (int k = 0; k < len; k++) {
            if (A.charAt(i + k) == B.charAt(j + k)) {
                count++;
            } else if (count > 0) {
                if (count > max) {
                    max = count;
                    pos = j + k;
                }
                count = 0;
            }
        }
        //在上面的for循环中，当k=len-1时,count可能也++了，所以下面的代码还要做个特殊判断
        if (count > 0 && count > max) {
            max = count;
            pos = j + len;
        }
        return B.substring(pos - max, pos);
    }
}
