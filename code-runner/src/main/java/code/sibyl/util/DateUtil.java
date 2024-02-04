package code.sibyl.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil extends DateUtils {

    public static final String yyyy_MM_dd = "yyyy-MM-dd";


    /**
     * 无异常解析时间字符串
     */
    public static Date parseDate(final String str, final String parsePattern) {
        try {
            return DateUtils.parseDate(str, parsePattern);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  某天的最早时间戳
     */
    public static Long minTimeOfDate(String yyyy_MM_dd) {
        return parseDate(yyyy_MM_dd.trim() + " 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
    }
    /**
     *  某天的最晚时间戳
     */
    public static Long maxTimeOfDate(String yyyy_MM_dd) {
        return parseDate(yyyy_MM_dd.trim() + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime();
    }

    /**
     *  某天的月开始天
     */
    public static String getFistDateOfMonth(String targetDate) {
        try {
            Date date = DateUtil.parseDate(targetDate.trim(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
            return targetDate;
        }
    }

    public static void main(String[] args) {
        String targetDate = "2023-12-01";

        System.err.println(targetDate);
        System.err.println(getFistDateOfMonth(targetDate));
    }

    /**
     * 某天的年开始天
     */
    public static String getFistDateOfYear(String targetDate) {
        try {
            Date date = DateUtil.parseDate(targetDate.trim(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            calendar.set(Calendar.MONTH, 1);
            return DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
            return targetDate;
        }
    }
}
