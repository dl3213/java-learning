package code.sibyl;

import code.sibyl.service.backup.BackupService;
import code.sibyl.service.sql.PostgresqlService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostgresqlTest {

    @Test
    public void backup() {
        BackupService.getBean().backup("postgres", PostgresqlService.getBean().template()).block();
    }
}
