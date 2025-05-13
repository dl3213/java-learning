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
public class MysqlMetadata implements Metadata {
    private String name;
    private String dbName;
    private List<String> tableNameList;
    private DatabaseClient databaseClient;
    private String queryTableListSql = """
            show tables;     
             """;

    public MysqlMetadata(String dbName, DatabaseClient databaseClient) {
        this.dbName = dbName;
        this.databaseClient = databaseClient;
    }
    public MysqlMetadata(String dbName, String name, DatabaseClient databaseClient) {
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
                .map(item -> String.valueOf(item.get(STR."Tables_in_\{this.dbName}")));
    }

    @Override
    public DatabaseClient.GenericExecuteSpec tableData(String tableName) {
        return this.databaseClient.sql(STR."select * from \{tableName}");
    }


    public static Mono<Metadata> build(String dbName, String name, DatabaseClient databaseClient) {
        return Mono.just(new MysqlMetadata(dbName, name, databaseClient));
    }


}
