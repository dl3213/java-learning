package code.sibyl

import code.sibyl.service.ClipImageSimilarityService
import code.sibyl.service.ImageSimilarityService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * ImageSimilarityService 测试
 */
@SpringBootTest
class ImageSimilarityServiceTest {

    @Autowired(required = false)
    var service: ClipImageSimilarityService? = null

    @Test
    fun testMatchTwoImagesById() {

        val id1 = 1973604240496660480L //
//        val id2 = 1973604240949645312L // 0.9119
        val id2 = 1973622335885938688L // 0.7976

        val result = service?.matchTwoImagesById(id1, id2)?.block()

        println("=== Match Result ===")
        result?.forEach { (k, v) -> println("$k: $v") }
    }
}
