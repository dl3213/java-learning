package code.sibyl.service.backup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class H2Metadata implements Metadata {
    private String name;
    private String dbName;
    private List<String> tableNameList;
    private DatabaseClient databaseClient;
    private String queryTableListSql = """
            show tables;     
             """;

    public H2Metadata(String dbName, DatabaseClient databaseClient) {
        this.dbName = dbName;
        this.databaseClient = databaseClient;
    }

    @Override
    public String dbName() {
        return this.dbName;
    }

    @Override
    public Flux<String> tableNameList() {
        return this.databaseClient.sql(this.queryTableListSql)
                .fetch()
                .all()
                .map(item -> String.valueOf(item.get("TABLE_NAME")));
    }

    @Override
    public DatabaseClient.GenericExecuteSpec tableData(String tableName) {
        return this.databaseClient.sql(STR."select * from \{tableName}");
    }


    public static Mono<Metadata> build(String name, DatabaseClient databaseClient) {
        return Mono.just(new H2Metadata(name, databaseClient));
    }


}
