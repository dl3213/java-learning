package code.sibyl.common

import cn.hutool.core.lang.Snowflake
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.boot.system.ApplicationHome
import org.springframework.core.env.Environment
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.reflect.Method
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URLEncoder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream


/**
 * 通用快捷函数
 */
object r {

    const val systemName: String = "未命名"
    const val staticFileBasePath: String = "/static-file/**"
    const val yyyy_MM_dd: String = "yyyy-MM-dd" //常用时间格式
    const val HH_mm_ss: String = "HH:mm:ss" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss: String = "yyyy-MM-dd HH:mm:ss" //常用时间格式
    const val yyyy_MM_dd_HH_mm_ss_SSSSSS: String = "yyyy-MM-dd HH:mm:ss.SSSSSS" //常用时间格式

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
    fun base64Encoder(): Base64.Encoder {
        return base64Encoder;
    }

    @JvmStatic
    fun base64Decoder(): Base64.Decoder {
        return base64Decoder;
    }

    @JvmStatic
    fun absolutePath(): String {
        return ApplicationHome().dir.absolutePath;
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

    @Deprecated(message = "")
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
    fun fileBaseDir(): String? {
        if ("prod".equals(r.env())) {
            return r.absolutePath() + File.separator + "file" + File.separator
        } else {
            return r.getBean(Environment::class.java).getProperty("file.path");
        }

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

    @JvmStatic
    fun formatDate(date: Date?, formatter: String?): String {
        return DateFormatUtils.format(date, formatter)
    }

    @JvmStatic
    fun formatDate(date: LocalTime?, formatter: String?): String {
        return DateTimeFormatter.ofPattern(formatter).format(date)
    }

    @JvmStatic
    fun formatDate(date: LocalDate?, formatter: String?): String {
        return DateTimeFormatter.ofPattern(formatter).format(date)
    }

    @JvmStatic
    fun formatDate(date: LocalDateTime?, formatter: String?): String {
        return DateTimeFormatter.ofPattern(formatter).format(date)
    }


    /**
     * 字符串处理
     */
    @JvmStatic
    fun string(obj: String?): String {
        return StringUtils.trim(obj)
    }

    @JvmStatic
    fun eqZero(vararg value: BigDecimal): Boolean {
        return Arrays.stream(value).allMatch { e: BigDecimal -> e.compareTo(BigDecimal.ZERO) == 0 }
    }

    /**
     * bigDecimal 空处理
     */
    @JvmStatic
    fun bigDecimal(obj: BigDecimal): BigDecimal {
        return if (Objects.nonNull(obj)) obj else BigDecimal.ZERO
    }

    @JvmStatic
    fun bigDecimal(obj: Any): BigDecimal {
        return if (Objects.nonNull(obj)) BigDecimal(obj.toString()) else BigDecimal.ZERO
    }

    @JvmStatic
    fun bigDecimal(obj: Int): BigDecimal {
        return if (Objects.nonNull(obj)) BigDecimal.valueOf(obj.toLong()) else BigDecimal.ZERO
    }

    @JvmStatic
    fun bigDecimal(obj: Long): BigDecimal {
        return if (Objects.nonNull(obj)) BigDecimal.valueOf(obj) else BigDecimal.ZERO
    }

    /**
     * bigDecimal 空处理
     */
    @JvmStatic
    fun bigDecimal(): BigDecimal {
        return BigDecimal.ZERO
    }

    /**
     * bigDecimal 除法处理
     */
    @JvmStatic
    fun divide(a: BigDecimal, b: BigDecimal): BigDecimal {
        return if (Objects.isNull(b) || b.compareTo(BigDecimal.ZERO) == 0 || Objects.isNull(a) || a.compareTo(BigDecimal.ZERO) == 0) BigDecimal.ZERO else a.divide(
            b, 4, BigDecimal.ROUND_UP
        )
    }

    /**
     * integer 空处理
     */
    @JvmStatic
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

    @JvmStatic
    fun <T> getBean(clazz: Class<T>?, name: String?): T {
        return SpringUtil.getBean(name, clazz)
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

    @JvmStatic
    fun defaultUserId(): Long {
        return 0L;
    }
    @JvmStatic
    fun id(): Long {
        return r.getBean(Snowflake::class.java).nextId();
    }
    @JvmStatic
    fun nextId(): Long {
        return getBean(Snowflake::class.java).nextId()
    }

    @JvmStatic
    fun dataToInsertSql(tableName: String, data: MutableMap<String, Any>): String {
        var sql: String = "INSERT INTO $tableName ("
        sql += data.entries.stream().map { it.key }.collect(Collectors.joining(","))
        sql += ")"
        sql += " VALUES ("
        sql += data.entries.stream()
            .map { if (Objects.isNull(it.value)) "null" else "'" + it.value.toString().replace("'", "") + "'" }
            .collect(Collectors.joining(","))
        sql += ");\n"
        return sql;
    }

    @JvmStatic
    fun env(): Any? {
        return System.getProperty("env") ?: "dev"
    }

    @JvmStatic
    fun percent(num: Any): String {
        return (r.bigDecimal(num).multiply(BigDecimal(100))).setScale(4, RoundingMode.UP).toString() + "%"
    }


    @JvmStatic
    fun isSql(obj: Any): Boolean {
        if (obj == null || obj.toString().isBlank()) return false;
        try {
            return CCJSqlParserUtil.parse(obj.toString()) != null;
        } catch (e: java.lang.Exception) {
            //e.printStackTrace()
            e.message?.let { error(it) }
            return false;
        }
    }

    @JvmStatic
    fun getAllFiles(directory: String?): Flux<String> {
        val startPath: Path = Paths.get(directory)
        return Flux.using(
            {
                Files.walk(startPath)
                    .filter { Files.isRegularFile(it) }
                    .sorted(compareBy { it.fileName.toString() })
            },
            { stream: Stream<Path> -> Flux.fromStream(stream) },  // 转换为 Flux<Path>
            { stream: Stream<*> -> stream.close() }// 资源清理
        )
            .filter(Files::isRegularFile) // 过滤出文件（排除目录）
            .map(Path::toString);
    }

    @JvmStatic
    fun walk(directory: String?, dept: Int): Flux<Path> {
        val startPath: Path = Paths.get(directory)
        return Flux.using(
            {
                Files.walk(startPath, dept)
            },
            { stream: Stream<Path> -> Flux.fromStream(stream) },  // 转换为 Flux<Path>
            { stream: Stream<*> -> stream.close() }// 资源清理
        )
//            .onErrorResume{
//                println("error => "+ it.message)
//                Mono.just(startPath)
//            }
    }

    @JvmStatic
    fun close(inputStream: InputStream?) {
        if (null != inputStream) {
            try {
                inputStream.close()
            } catch (e: java.lang.Exception) {
            }
        }
    }
    @JvmStatic
    fun urlDecode(str: String): String {
        return URLEncoder.encode(str, "UTF-8");
    }

    @JvmStatic
    fun snakeToCamel(snakeStr: String): String {
        return snakeStr.replace("_(.)".toRegex()) {
            if (it.groupValues.size > 1) it.groupValues[1].uppercase() else ""
        }
    }

    @JvmStatic
    fun long2localDateTime(long: Long?): LocalDateTime? {
        if (long == null) return null;
        // 创建Instant对象
        val instant = Instant.ofEpochMilli(long)
        // 转换为OffsetDateTime，这里使用UTC偏移量（你也可以使用其他偏移量）
        val offsetDateTime = instant.atZone(ZoneOffset.systemDefault()) // 系统默认时区
        // 从OffsetDateTime转换为LocalDateTime（这会去掉偏移量）
        return offsetDateTime.toLocalDateTime()
    }
    @JvmStatic
    fun long2localDateTime(long: Long?, zoneId: ZoneId): LocalDateTime? {
        if (long == null) return null;
        // 创建Instant对象
        val instant = Instant.ofEpochMilli(long)
        // 转换为OffsetDateTime，这里使用UTC偏移量（你也可以使用其他偏移量）
        val offsetDateTime = instant.atZone(zoneId) // 系统默认时区
        // 从OffsetDateTime转换为LocalDateTime（这会去掉偏移量）
        return offsetDateTime.toLocalDateTime()
    }

    @JvmStatic
    fun str2localDateTime(str: String?, formatter: String?): LocalDateTime? {
        try {
            var dateTime = cn.hutool.core.date.DateUtil.parse(str as CharSequence, formatter)
            return dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}

fun main() {
    var long = 1772579346000L
    println(long.toString().length)
    println(r.long2localDateTime(long, ZoneId.of("UTC-4")))
    println(r.long2localDateTime(long))

    println()
    var currentTimeMillis = System.currentTimeMillis()
    println(currentTimeMillis)
    println(r.long2localDateTime(currentTimeMillis))
    println()

    println(r.long2localDateTime(1609459200000L))

    println()

    val timestamp = 1772579346000L // UTC 2023-12-31T12:00:00Z
    val dateTimeUtc = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.of("UTC-04")
    )
    println("UTC 时间: $dateTimeUtc") // 2023-12-31T12:00


    // 方案2：转换为目标时区（如上海时间 UTC+8）
    val shanghaiTime = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.of("Asia/Shanghai"))
    val dateTimeShanghai = shanghaiTime.toLocalDateTime()
    println("上海时间: $dateTimeShanghai")
}
