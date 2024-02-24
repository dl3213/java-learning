package code.sibyl.util;

import code.sibyl.common.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 通用快捷函数
 */
public class r {

    public static final String yyyy_MM_dd = "yyyy-MM-dd";//常用时间格式
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd";//常用时间格式

    public static final String yyyy_MM = "yyyy-MM";//常用时间格式

    public static final String yyyy = "yyyy";//常用时间格式
    public static final String MM = "MM";//常用时间格式

    public static Response success(Object data) {
        return Response.success(data);
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

    public static <T> List<T> copy(T obj, int num) {
        List<T> list = new ArrayList<>();
        //list.add(obj);
        for (int i = 0; i < num; i++) {
            list.add(r.copy(obj));
        }
        return list;
    }

    public static <T> T copy(T obj) {
        if (Objects.isNull(obj)) return null;
        try {
            T copy = (T) obj.getClass().newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(copy, field.get(obj));
            }
            return copy;
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
//    public static ThreadPoolTaskExecutor taskExecutor() {
//        return SpringUtil.getBean(ThreadPoolTaskExecutor.class);
//    }

    /**
     * 字符串处理
     */
    public static String string(String obj) {
        return StringUtils.trim(obj);
    }

    public static boolean eqZero(BigDecimal... value) {
        return Arrays.stream(value).allMatch(e -> e.compareTo(BigDecimal.ZERO) == 0);
    }

    /**
     * bigDecimal 空处理
     */
    public static BigDecimal bigDecimal(BigDecimal obj) {
        return Objects.nonNull(obj) ? obj : BigDecimal.ZERO;
    }

    public static BigDecimal bigDecimal(Object obj) {
        return Objects.nonNull(obj) ?
                new BigDecimal(StringUtils.isNumeric(obj.toString()) ? obj.toString() : "0") : BigDecimal.ZERO;
    }

    public static BigDecimal bigDecimal(int obj) {
        return Objects.nonNull(obj) ? BigDecimal.valueOf(obj) : BigDecimal.ZERO;
    }

    public static BigDecimal bigDecimal(long obj) {
        return Objects.nonNull(obj) ? BigDecimal.valueOf(obj) : BigDecimal.ZERO;
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
                BigDecimal.ZERO : a.divide(b, 4, BigDecimal.ROUND_UP);
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
            Date date = DateUtil.parseDate(targetDate.trim(), sdf);
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
            Date date = DateUtil.parseDate(targetDate.trim(), sdf);
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

    public static Function curry(Function function) {
        // todo
        return function;
    }

    public static boolean equals(Object a, Object b) {
        return Objects.nonNull(a) && Objects.nonNull(b) && Objects.equals(a, b);
    }

    public static boolean equals(BigDecimal a, BigDecimal b) {
        return Objects.nonNull(a) && Objects.nonNull(b) && a.compareTo(b) == 0;
    }

    public static boolean validated(Function<BigDecimal, BigDecimal>... function) {
//        Function<BigDecimal, BigDecimal> f = s -> s;
//        for (Function<BigDecimal, BigDecimal> func : function) {
//            f = f.andThen(func);
//        }
        return true;
    }

    public static boolean predicate(BigDecimal a, BigDecimal b, BiPredicate<BigDecimal, BigDecimal> predicate) {
        return predicate.test(a, b);
    }

    public static void main(String[] args) {

//        System.err.println(QueryMap.builder().orgCode("3201"));
//        System.err.println(QueryMap.builder().supplierCode("测试"));
//        System.err.println(r.predicate(BigDecimal.ZERO, BigDecimal.valueOf(0.0d), r::equals));
//        System.err.println(r.predicate(BigDecimal.ZERO, BigDecimal.valueOf(2.3d), r::equals));
//
//        fori:
//        for (int i = 0; i < 10; i++) {
//            System.err.println("i=" + i);
//            forj:
//            for (int j = 0; j < 5; j++) {
//                if (j == 2) {
//                    break fori;
//                }
//                System.err.println("j=" + j);
//
//            }
//        }

//        System.err.println(r.equalsBigDecimal(BigDecimal.ZERO, BigDecimal.valueOf(0.0d)));
//        System.err.println(r.equalsBigDecimal(BigDecimal.ZERO, BigDecimal.valueOf(2.3d)));

//        r.validated(BigDecimal.ZERO, BigDecimal.valueOf(2.3d), BigDecimal::equals);

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

//    class Validated<T> {
//        private T target;
//        private T[] resources;
//
//    }
}
