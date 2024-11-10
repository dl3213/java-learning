package code.sibyl.common

import org.apache.commons.lang3.time.DateFormatUtils
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.security.MessageDigest
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

    open var baseDir: String = "D:/4code/4java/workspace/java-learning/kotlin-runner/file";
    const val systemName: String = "未命名 " //常用时间格式
    const val yyyy_MM_dd: String = "yyyy-MM-dd" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss: String = "yyyy-MM-dd HH:mm:ss" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss_SSS: String = "yyyy-MM-dd HH:mm:ss.[SSS]" //常用时间格式
    const val fileBaseDir: String = "E:/4me/pixiv/pixez/"

    const val yyyy_MM: String = "yyyy-MM" //常用时间格式

    const val yyyy: String = "yyyy" //常用时间格式
    const val MM: String = "MM" //常用时间格式

    val base64Encoder: Base64.Encoder = Base64.getEncoder();
    val base64Decoder: Base64.Decoder = Base64.getDecoder();

    @JvmStatic
    fun base64Encoder(): Base64.Encoder? {
        return base64Encoder;
    }

    @JvmStatic
    fun base64Decoder(): Base64.Decoder? {
        return base64Decoder;
    }


    @JvmStatic
    fun baseDir(): String? {
        return this.baseDir;
    }

    @JvmStatic
    fun computeFileSHA256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        FileInputStream(file).use { fis ->
            val buffer = ByteArray(1024)
            var numRead = 0
            while (fis.read(buffer).also { numRead = it } > 0) {
                digest.update(buffer, 0, numRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    @JvmStatic
    fun format(time: LocalDateTime, formatter: String?): String? {
        return DateTimeFormatter.ofPattern(formatter).format(time);
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
    fun error(msg: String): Response {
        return Response.error(msg)
    }


    fun formatDate(date: LocalDate?, formatter: String?): String {
        return DateTimeFormatter.ofPattern(formatter).format(date)
    }

    fun formatDate(date: LocalDateTime?, formatter: String?): String {
        return DateTimeFormatter.ofPattern(formatter).format(date)
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
    }
}
