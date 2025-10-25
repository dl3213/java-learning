package code.sibyl

import code.sibyl.common.r
import code.sibyl.service.UpdateService
import code.sibyl.service.backup.BackupService
import code.sibyl.service.sql.PostgresqlService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.kotlin.core.publisher.toFlux
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@SpringBootTest
class KotlinApplicationTests {


    @Test
    open fun contextLoads() {
        UpdateService.getBean().视频文件补充thumbnail().block();
    }

    @Test
    open fun pixiv_hash_clear() {
        var client = PostgresqlService.getBean().template().databaseClient

        client.sql(
            """
            select sha256 from (
                    select sha256, count(1) as count from T_BASE_FILE
                    where is_deleted = '0' 
                    and sha256 is not null
                    and code = 'pixiv'
                    group by sha256
                    )t where count >=2 
        """.trimIndent()
        )
            .fetch()
            .all()
            .flatMap {
                client.sql(
                    """
                    select * 
                    from t_base_file 
                    where is_deleted = '0' 
                    and code = 'pixiv'
                    and sha256 =:sha256
                    order by sha256 asc, create_time asc
                """.trimIndent()
                )
                    .bind("sha256", it.get("sha256") as String)
                    .fetch()
                    .all()
                    .collectList()
            }
            .flatMap {
                println(it[0]["sha256"] as String + " -> " + it.size)
                return@flatMap it.take(it.size - 1)
                    .toFlux()
                    .flatMap { item ->
                        client.sql("update t_base_file set is_deleted = '1' where id = :id ")
                            .bind("id", item.get("id") as Long)
                            .fetch()
                            .rowsUpdated()
                    }
                    .count()
            }
            .count()
            .doOnNext {
                println("共 ${it} 个")
            }
            .block()

    }

    @Test
    open fun backup() {
        BackupService.getBean().backup("sibyl", r.getBean(R2dbcEntityTemplate::class.java)).map { it -> println(it) }
            .block();
    }

    @Test
    open fun hash() {
        val file = File("")
        val sha256_1 = computeFileSHA256(file)
        println("SHA-256: $sha256_1")

        var file2byte = file2byte(file)
        var hashBytes = file2byte?.let { hashBytes(it) }
        println("SHA-256: $hashBytes")
    }

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

    fun file2byte(file: File): ByteArray? {
        return file.readBytes();
    }

    fun hashBytes(bytes: ByteArray): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(bytes)
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }
}
