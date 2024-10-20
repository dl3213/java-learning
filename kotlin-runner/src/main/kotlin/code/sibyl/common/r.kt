package code.sibyl.common

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.core.env.Environment
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import java.io.File
import java.io.FileInputStream
import java.lang.reflect.Method
import java.math.BigDecimal
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
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
    const val fileBaseDir: String = "E:/4me/r-18/" //常用时间格式
    const val yyyy_MM_dd: String = "yyyy-MM-dd" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss: String = "yyyy-MM-dd HH:mm:ss" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss_SSS: String = "yyyy-MM-dd HH:mm:ss.[SSS]" //常用时间格式

    const val yyyy_MM: String = "yyyy-MM" //常用时间格式

    const val yyyy: String = "yyyy" //常用时间格式
    const val MM: String = "MM" //常用时间格式

    private val IMAGE_EXTENSIONS = arrayOf("jpg", "jpeg", "png", "gif", "bmp", "webp")

    // 图片文件头的前几个字节
    private val IMAGE_FILE_HEADERS = arrayOf(
        byteArrayOf(0xFF.toByte(), 0xD8.toByte()),  // JPEG (jpg)
        byteArrayOf(0x89.toByte(), 0x50.toByte()),  // PNG (png)
        byteArrayOf(0x47.toByte(), 0x49.toByte(), 0x46.toByte()),  // GIF (gif)
        byteArrayOf(0x42.toByte(), 0x4D.toByte()),  // BMP (bmp)
        byteArrayOf(0x52.toByte(), 0x49.toByte(), 0x46.toByte(), 0x46.toByte()),  // WebP (webp)
    )

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
    fun yyyy_MM_dd(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(r.yyyy_MM_dd));
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
    fun file2byte(file: File): ByteArray? {
        return file.readBytes();
    }

    @JvmStatic
    fun hashBytes(bytes: ByteArray): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(bytes)
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }

    @JvmStatic
    fun isImage(file: File): Boolean {
        if (file == null || !file.exists()) return false

        // 先检查文件扩展名
        val extension: String = getFileExtension(file.name)?.toLowerCase(Locale.ENGLISH).toString()
        for (imgExtension in IMAGE_EXTENSIONS) {
            if (StringUtils.equals(extension, imgExtension)) {
                return true
            }
        }


        // 文件扩展名不匹配，再检查文件头
        val fis: FileInputStream = FileInputStream(file)
        val fileHeader = ByteArray(IMAGE_FILE_HEADERS[0].size)
        fis.read(fileHeader, 0, fileHeader.size)
        fis.close()
        for (header in IMAGE_FILE_HEADERS) {
            if (equals(fileHeader, header)) {
                return true
            }
        }

        return false

        return true;
    }

    private fun equals(a1: ByteArray, a2: ByteArray): Boolean {
        if (a1.size != a2.size) {
            return false
        }
        for (i in a1.indices) {
            if (a1[i] != a2[i]) {
                return false
            }
        }
        return true
    }

    private fun getFileExtension(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index = filename.lastIndexOf('.')
        if (index == -1) {
            return null
        }
        return filename.substring(index + 1)
    }

    // 辅助方法，用于判断方法是否返回Mono或Flux
    @JvmStatic
    private fun isReactiveReturnType(method: Method): Boolean {
        return (Mono::class.java.isAssignableFrom(method.returnType) || Flux::class.java.isAssignableFrom(method.returnType))
    }

    // 辅助方法，用于判断方法的参数是否是响应式的
    @JvmStatic
    private fun hasReactiveArguments(method: Method): Boolean {
        return Arrays.stream(method.parameters).anyMatch { param ->
            (Mono::class.java.isAssignableFrom(param.getType()) || Flux::class.java.isAssignableFrom(param.getType()))
        }
    }

    // 根据方法判断是否响应式
    @JvmStatic
    fun isReactive(method: Method): Boolean {
        return isReactiveReturnType(method) || hasReactiveArguments(method)
    }

    @JvmStatic
    fun getWebExchange(): Mono<ServerWebExchange> {
        return r.getMonoContext(ServerWebExchange::class.java);
    }

    @JvmStatic
    fun getWebExchange_flux(): Flux<ServerWebExchange> {
        return r.getFluxContext(ServerWebExchange::class.java);
    }

    @JvmStatic
    fun <T> getMonoContext(key: Class<T>): Mono<T> {
        return Mono.deferContextual { data: ContextView? ->
            Mono.just(
                data as ContextView
            )
        }.cast(
            Context::class.java
        ).filter { context: Context? ->
            context?.hasKey(key) == true
        }.map { context: Context? ->
            context?.get(key)
        }
    }

    @JvmStatic
    fun <T> getFluxContext(key: Class<T>): Flux<T> {
        return Flux.deferContextual { data: ContextView? ->
            Flux.just(
                data as ContextView
            )
        }.cast(
            Context::class.java
        ).filter { context: Context? ->
            context?.hasKey(key) == true
        }.map { context: Context? ->
            context?.get(key)
        }
    }

//    @JvmStatic
//    fun <T> getFluxContext(key: Class<T>): Mono<T> {
//        return Flux.deferContextual { data: ContextView? ->
//            Mono.just(
//                data as ContextView
//            )
//        }.cast(
//            Context::class.java
//        ).filter { context: Context? ->
//            context?.hasKey(key) == true
//        }.map { context: Context? ->
//            context?.get(key)
//        }
//    }

    @JvmStatic
    fun baseDir(): String? {
        return this.baseDir;
    }

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
    fun successMono(e: Any): Mono<Response> {
        return Mono.just(success(e));
    }

    @JvmStatic
    fun error(msg: String): Response {
        return Response.error(msg)
    }

    @JvmStatic
    fun errorMono(msg: String): Mono<Response> {
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
            b, 4, BigDecimal.ROUND_UP
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
    @JvmStatic
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
    }

    @JvmStatic
    fun defaultUserId(): Long {
        return 0L;
    }

}
