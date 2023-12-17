package code.sibyl.reactive.common;

import code.sibyl.reactive.util.SpringUtil;
import code.sibyl.reactive.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 通用快捷函数
 */
public class r {

    public static final String yyyy_MM_dd = "yyyy-MM-dd";//常用时间格式

    public static final String yyyy_MM = "yyyy-MM";//常用时间格式

    public static final String yyyy = "yyyy";//常用时间格式
    public static final String MM = "MM";//常用时间格式

    public static void main(String[] args) {

        System.err.println(System.getProperty("java.version"));


//        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
//
//        // 查询堆内存的使用情况
//        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
//
//        // 查询非堆内存的使用情况
//        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
//
//        System.out.println("Heap Memory Usage:");
//        System.out.println("  Used Memory: " + heapMemoryUsage.getUsed() + " bytes");// 获取最大内存大小（bytes）
//        System.out.println("  Committed Memory: " + heapMemoryUsage.getCommitted() + " bytes");// 获取已分配内存大小（bytes）
//        System.out.println("  Max Memory: " + heapMemoryUsage.getMax() + " bytes");// 获取已使用内存大小（bytes）
//
//        Object o = new Object();
//
//        System.out.println("Heap Memory Usage:");
//        System.out.println("  Used Memory: " + heapMemoryUsage.getUsed() + " bytes");// 获取最大内存大小（bytes）
//        System.out.println("  Committed Memory: " + heapMemoryUsage.getCommitted() + " bytes");// 获取已分配内存大小（bytes）
//        System.out.println("  Max Memory: " + heapMemoryUsage.getMax() + " bytes");// 获取已使用内存大小（bytes）
//
//        o = null;
//        System.out.println("Heap Memory Usage:");
//        System.out.println("  Used Memory: " + heapMemoryUsage.getUsed() + " bytes");// 获取最大内存大小（bytes）
//        System.out.println("  Committed Memory: " + heapMemoryUsage.getCommitted() + " bytes");// 获取已分配内存大小（bytes）
//        System.out.println("  Max Memory: " + heapMemoryUsage.getMax() + " bytes");// 获取已使用内存大小（bytes）

//        System.err.println("start");
//        int i = 1;
//        r.runCode(() -> {
//            throw new RuntimeException("test");
//        });
//        System.err.println("end i =" + i);
    }

    /**
     *
     */

    public static void cleanUp(Object obj) {
        if (null == obj) return;
        obj = null;
    }

    public static Void runCode(Supplier<Void> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(Date date, String formatter) {
        return DateFormatUtils.format(date, formatter);
    }

    /**
     *
     */
    public static ThreadPoolTaskExecutor taskExecutor() {
        return SpringUtil.getBean(ThreadPoolTaskExecutor.class);
    }

    /**
     * 字符串处理
     */
    public static String string(String obj) {
        return StringUtils.trim(obj);
    }

    /**
     * bigDecimal 空处理
     */
    public static BigDecimal bigDecimal(BigDecimal obj) {
        return Objects.nonNull(obj) ? obj : BigDecimal.ZERO;
    }

    /**
     * bigDecimal 空处理
     */
    public static BigDecimal bigDecimal() {
        return BigDecimal.ZERO;
    }

    /**
     * bigDecimal 除法处理
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return Objects.isNull(b)
                || b.compareTo(BigDecimal.ZERO) == 0
                || Objects.isNull(a)
                || a.compareTo(BigDecimal.ZERO) == 0 ?
                BigDecimal.ZERO : a.divide(b, 4, RoundingMode.HALF_UP);
    }

    /**
     * integer 空处理
     */
    public static Integer integer(Integer obj) {
        return Objects.nonNull(obj) ? obj : 0;
    }

    /**
     * 线程等待
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户id
     */
    public static String getUserId() {
        return UserUtil.username();
    }

    /**
     * 获取bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return SpringUtil.getBean(clazz);
    }

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
     * 某天的最早时间戳
     */
    public static Long minTimeOfDate(String yyyy_MM_dd) {
        return parseDate(yyyy_MM_dd.trim() + " 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
    }

    /**
     * 某天的最晚时间戳
     */
    public static Long maxTimeOfDate(String yyyy_MM_dd) {
        return parseDate(yyyy_MM_dd.trim() + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime();
    }

    /**
     * 某天的月开始天
     */
    public static String getFistDateOfMonth(String targetDate) {
        return getFistDateOfMonth(targetDate, r.yyyy_MM_dd);
    }

    public static String getFistDateOfMonth(String targetDate, String sdf) {
        try {
            Date date = DateUtils.parseDate(targetDate.trim(), sdf);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return DateFormatUtils.format(calendar.getTime(), r.yyyy_MM_dd);
        } catch (Exception e) {
            e.printStackTrace();
            return targetDate;
        }
    }

    public static String getLastDateOfMonth(String targetDate, String sdf) {
        try {
            Date date = DateUtils.parseDate(targetDate.trim(), sdf);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return DateFormatUtils.format(calendar.getTime(), r.yyyy_MM_dd);
        } catch (Exception e) {
            e.printStackTrace();
            return targetDate;
        }
    }


    /**
     * 某天的年开始天
     */
    public static String getFistDateOfYear(String targetDate) {
        try {
            Date date = DateUtils.parseDate(targetDate.trim(), "yyyy-MM-dd");
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
