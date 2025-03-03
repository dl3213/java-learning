package code.sibyl

import code.sibyl.domain.base.BaseFile
import code.sibyl.domain.database.Database
import code.sibyl.domain.sys.User
import code.sibyl.service.sql.H2Service
import code.sibyl.service.sql.PostgresqlService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query

@SpringBootTest
class KotlinTests {


    @Test
    open fun javaTest() {
        H2Service.getBean().template().select(Query.query(Criteria.empty()), Database::class.java)
            .flatMap {
                println(it)
                PostgresqlService.getBean().template().insert(it);
            }
            .count()
            .map {
                System.err.println("count => $it")
            }
            .block()
    }

}
