package code.sibyl.common

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.core.env.Environment
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.BiPredicate
import java.util.function.Function


/**
 * 通用快捷函数
 */
object r {
    const val systemName: String = "未命名 " //常用时间格式
    const val yyyy_MM_dd: String = "yyyy-MM-dd" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss: String = "yyyy-MM-dd HH:mm:ss" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss_SSS: String = "yyyy-MM-dd HH:mm:ss.[SSS]" //常用时间格式

    const val yyyy_MM: String = "yyyy-MM" //常用时间格式

    const val yyyy: String = "yyyy" //常用时间格式
    const val MM: String = "MM" //常用时间格式

    @JvmStatic
    fun systemName(): String? {
        return getBean(Environment::class.java).getProperty("spring.application.name");
    }

    @JvmStatic
    fun success(data: Any?): Response {
        return Response.success(data)
    }
    @JvmStatic
    fun success(): Response {
        return Response.success()
    }
    @JvmStatic
    fun successMono(): Mono<Response> {
        return Mono.just(success());
    }
    @JvmStatic
    fun successMono(e : Any): Mono<Response> {
        return Mono.just(success(e));
    }
    @JvmStatic
    fun error(msg : String): Response {
        return Response.error(msg)
    }
    @JvmStatic
    fun errorMono(msg : String): Mono<Response> {
        return Mono.just(Response.error(msg));
    }

    fun formatDate(date: Date?, formatter: String?): String {
        return DateFormatUtils.format(date, formatter)
    }

    fun formatDate(date: LocalDate?, formatter: String?): String {
        return DateTimeFormatter.ofPattern(formatter).format(date)
    }

    fun formatDate(date: LocalDateTime?, formatter: String?): String {
        return DateTimeFormatter.ofPattern(formatter).format(date)
    }


    /**
     * 字符串处理
     */
    fun string(obj: String?): String {
        return StringUtils.trim(obj)
    }

    fun eqZero(vararg value: BigDecimal): Boolean {
        return Arrays.stream(value).allMatch { e: BigDecimal -> e.compareTo(BigDecimal.ZERO) == 0 }
    }

    /**
     * bigDecimal 空处理
     */
    fun bigDecimal(obj: BigDecimal): BigDecimal {
        return if (Objects.nonNull(obj)) obj else BigDecimal.ZERO
    }

    fun bigDecimal(obj: Any): BigDecimal {
        return if (Objects.nonNull(obj)) BigDecimal(if (StringUtils.isNumeric(obj.toString())) obj.toString() else "0") else BigDecimal.ZERO
    }

    fun bigDecimal(obj: Int): BigDecimal {
        return if (Objects.nonNull(obj)) BigDecimal.valueOf(obj.toLong()) else BigDecimal.ZERO
    }

    fun bigDecimal(obj: Long): BigDecimal {
        return if (Objects.nonNull(obj)) BigDecimal.valueOf(obj) else BigDecimal.ZERO
    }

    /**
     * bigDecimal 空处理
     */
    fun bigDecimal(): BigDecimal {
        return BigDecimal.ZERO
    }

    /**
     * bigDecimal 除法处理
     */
    fun divide(a: BigDecimal, b: BigDecimal): BigDecimal {
        return if (Objects.isNull(b) || b.compareTo(BigDecimal.ZERO) == 0 || Objects.isNull(a) || a.compareTo(BigDecimal.ZERO) == 0) BigDecimal.ZERO else a.divide(
            b,
            4,
            BigDecimal.ROUND_UP
        )
    }

    /**
     * integer 空处理
     */
    fun integer(obj: Int): Int {
        return if (Objects.nonNull(obj)) obj else 0
    }

    /**
     * 线程等待
     */
    fun sleep(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    /**
     * 获取bean
     */
    @JvmStatic
    fun <T> getBean(clazz: Class<T>?): T {
        return SpringUtil.getBean(clazz)
    }

    /**
     * 无异常解析时间字符串
     */
    @JvmStatic
    fun parseDate(str: String?, parsePattern: String?): Date? {
        try {
            return DateUtils.parseDate(str, parsePattern)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 某天的最早时间戳
     */
    fun minTimeOfDate(yyyy_MM_dd: String): Long {
        return parseDate(yyyy_MM_dd.trim { it <= ' ' } + " 00:00:00", "yyyy-MM-dd HH:mm:ss")!!.time
    }

    /**
     * 某天的最晚时间戳
     */
    fun maxTimeOfDate(yyyy_MM_dd: String): Long {
        return parseDate(yyyy_MM_dd.trim { it <= ' ' } + " 23:59:59", "yyyy-MM-dd HH:mm:ss")!!.time
    }

    /**
     * 某天的月开始天
     */
    fun getFistDateOfMonth(targetDate: String): String {
        return getFistDateOfMonth(targetDate, yyyy_MM_dd)
    }

    fun getFistDateOfMonth(targetDate: String, sdf: String?): String {
        try {
            val date = DateUtils.parseDate(targetDate.trim { it <= ' ' }, sdf)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.DAY_OF_MONTH] = 1
            return DateFormatUtils.format(calendar.time, yyyy_MM_dd)
        } catch (e: Exception) {
            e.printStackTrace()
            return targetDate
        }
    }

    fun getLastDateOfMonth(targetDate: String, sdf: String?): String {
        try {
            val date = DateUtils.parseDate(targetDate.trim { it <= ' ' }, sdf)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            return DateFormatUtils.format(calendar.time, yyyy_MM_dd)
        } catch (e: Exception) {
            e.printStackTrace()
            return targetDate
        }
    }


    /**
     * 某天的年开始天
     */
    fun getFistDateOfYear(targetDate: String): String {
        try {
            val date = DateUtils.parseDate(targetDate.trim { it <= ' ' }, "yyyy-MM-dd")
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.DAY_OF_MONTH] = 0
            calendar[Calendar.MONTH] = 1
            return DateFormatUtils.format(calendar.time, "yyyy-MM-dd")
        } catch (e: Exception) {
            e.printStackTrace()
            return targetDate
        }
    }

    fun curry(function: Function<*, *>): Function<*, *> {
        // todo
        return function
    }

    fun equals(a: Any, b: Any): Boolean {
        return Objects.nonNull(a) && Objects.nonNull(b) && a == b
    }

    fun equals(a: BigDecimal, b: BigDecimal?): Boolean {
        return Objects.nonNull(a) && Objects.nonNull(b) && a.compareTo(b) == 0
    }

    fun validated(vararg function: Function<BigDecimal?, BigDecimal?>?): Boolean {
//        Function<BigDecimal, BigDecimal> f = s -> s;
//        for (Function<BigDecimal, BigDecimal> func : function) {
//            f = f.andThen(func);
//        }
        return true
    }

    fun predicate(a: BigDecimal?, b: BigDecimal?, predicate: BiPredicate<BigDecimal?, BigDecimal?>): Boolean {
        return predicate.test(a, b)
    }

    fun main123(args: Array<String?>?) {
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
    } //    class Validated<T> {
    //        private T target;
    //        private T[] resources;
    //
    //    }
}
