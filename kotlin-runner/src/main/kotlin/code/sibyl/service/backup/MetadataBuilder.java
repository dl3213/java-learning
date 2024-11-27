package code.sibyl.service.backup;

import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

public class MetadataBuilder {

    public static Mono<Metadata> build(String name, DatabaseClient databaseClient) {
        switch (name) {
            case "H2" -> {
                return H2Metadata.build(name, databaseClient);
            }
            default -> throw new RuntimeException();
        }
    }
}
