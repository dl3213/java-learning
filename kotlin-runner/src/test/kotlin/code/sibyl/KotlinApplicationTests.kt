package code.sibyl

import code.sibyl.common.r
import code.sibyl.service.DataBaseService
import code.sibyl.service.UpdateService
import code.sibyl.service.backup.BackupService
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
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
