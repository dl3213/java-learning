package code.sibyl.service.backup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class H2Metadata implements Metadata {
    private String name;
    private String dbName;
    private List<String> tableNameList;


    @Override
    public String dbName() {
        return this.dbName;
    }

    @Override
    public List<String> tableNameList() {
        return this.tableNameList;
    }


    public static Mono<H2Metadata> build(String name, DatabaseClient databaseClient) {
        String sql = "show tables;";
        return databaseClient.sql(sql)
                .fetch()
                .all()
                .collectList()
                .map(list -> {
                    H2Metadata metadata = new H2Metadata();
                    metadata.setName(name);
                    metadata.setTableNameList(list.stream().map(e -> String.valueOf(e.get("TABLE_NAME"))).collect(Collectors.toList()));
                    return metadata;
                });

    }
}
