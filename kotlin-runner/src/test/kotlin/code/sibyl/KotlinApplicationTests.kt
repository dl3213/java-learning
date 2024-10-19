package code.sibyl

import code.sibyl.common.r
import code.sibyl.service.DataBaseService
import code.sibyl.service.UpdateService
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
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
        println("test")
        var bean = r.getBean(DataBaseService::class.java)
        var list = bean.list()
    }

    @Test
    open fun hash() {
        val file = File("E:\\4me\\pixez\\-1fc1a621974db39c.gif")
        val file2 = File("D:\\-1fc1a621974db39c.gif")
        val file3 = File("E:\\4me\\pixez\\2230077_p0.jpg")
        val sha256_1 = computeFileSHA256(file)
        val sha256_2 = computeFileSHA256(file2)
        val sha256_3 = computeFileSHA256(file3)
        println("SHA-256: $sha256_1")
        println("SHA-256: $sha256_2")
        println("SHA-256: $sha256_3")

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
