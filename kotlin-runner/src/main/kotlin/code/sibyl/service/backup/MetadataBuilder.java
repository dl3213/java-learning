package code.sibyl.service.backup;

import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

public class MetadataBuilder {

    public static Mono<Metadata> build(String dbName, String name, DatabaseClient databaseClient) {
        switch (name) {
            case "H2" -> {
                return H2Metadata.build(dbName, name, databaseClient);
            }
            case "MySQL" -> {
                return MysqlMetadata.build(dbName, name, databaseClient);
            }
            default -> throw new NullPointerException();
        }
    }
}
