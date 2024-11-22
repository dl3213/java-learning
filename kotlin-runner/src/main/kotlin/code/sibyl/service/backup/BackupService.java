package code.sibyl.service.backup;

import code.sibyl.common.r;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class BackupService {

    public static BackupService getBean() {
        return r.getBean(BackupService.class);
    }

    public Mono<Long> backup(R2dbcEntityTemplate r2dbcEntityTemplate) {
        return backup(r2dbcEntityTemplate.getDatabaseClient());
    }

    public Mono<Long> backup(DatabaseClient databaseClient) {
        ConnectionFactory connectionFactory = databaseClient.getConnectionFactory();
        String name = connectionFactory.getMetadata().getName();

        System.err.println(name);
        ApplicationHome applicationHome = new ApplicationHome(getClass());
        String absolutePath = applicationHome.getDir().getAbsolutePath();
        System.err.println(STR."ApplicationHome -> \{absolutePath}");
        String classPath = ClassLoader.getSystemResource("").getPath();
        System.err.println(STR."classPath -> \{absolutePath}");
        String projectPath = new File("").getAbsolutePath();
        System.err.println(STR."projectPath -> \{absolutePath}");


        return MetadataBuilder.build(name, databaseClient)
                .map(metadata -> {
                    System.err.println(metadata);
                    System.err.println(metadata.getTableNameList());
                    return metadata;
                })
                .then().thenReturn(1L);

    }

}
