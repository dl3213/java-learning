package code.sibyl.service.backup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostgresMetadata implements Metadata {
    private String name;
    private String dbName;
    private List<String> tableNameList;
    private DatabaseClient databaseClient;
    private String queryTableListSql = """
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = 'public'  -- 通常我们只关心public schema
            and table_type = 'BASE TABLE'
            ORDER BY table_name;   
             """;

    public PostgresMetadata(String dbName, DatabaseClient databaseClient) {
        this.dbName = dbName;
        this.databaseClient = databaseClient;
    }
    public PostgresMetadata(String dbName, String name, DatabaseClient databaseClient) {
        this.dbName = dbName;
        this.name = name;
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
                .map(item -> String.valueOf(item.get("table_name")));
    }

    @Override
    public DatabaseClient.GenericExecuteSpec tableData(String tableName) {
        return this.databaseClient.sql(STR."select * from \{tableName}");
    }


    public static Mono<Metadata> build(String dbName, String name, DatabaseClient databaseClient) {
        return Mono.just(new PostgresMetadata(dbName, name, databaseClient));
    }


}
