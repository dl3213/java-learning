package me.sibyl.test;

public class StrTest {

    public static void main(String[] args) {
        char a = '0';  // 最长字符
        int a_len = 0;  // 最长连续字符的长度
        char b = '0';  // 当前统计的字符
        int b_len = 1;  // 字符长度
        String s = "1234456xxxxxxxxxxxx9999fghjkyyyyy";  // 待统计的字符串

        b = s.charAt(0);
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 || s.charAt(i) == s.charAt(i - 1)) {
                b_len++;
            } else {
                if (b_len > a_len) {
                    a_len = b_len;
                    a = b;
                }

                b_len = 1;
                b = s.charAt(i);
            }
        }

        if (b_len > a_len) {
            a_len = b_len;
            a = b;
        }

        System.out.printf("%c%d", a, a_len);
    }
}
