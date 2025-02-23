package code.sibyl

import code.sibyl.service.Eos2Service
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KotlinTests {


    @Test
    open fun javaTest() {
        Eos2Service.getBean().test().block()
    }

}
