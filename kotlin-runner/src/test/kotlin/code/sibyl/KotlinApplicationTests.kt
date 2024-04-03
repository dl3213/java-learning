package code.sibyl

import code.sibyl.common.r
import code.sibyl.service.DataBaseService
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KotlinApplicationTests {

    @Test
    open fun contextLoads() {
        var bean = r.getBean(DataBaseService::class.java)
        var list = bean.list()
    }
}
