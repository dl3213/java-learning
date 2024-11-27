package code.sibyl.service.backup;

import org.reactivestreams.Publisher;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface Metadata {

    String dbName();

    Flux<String> tableNameList();

    DatabaseClient.GenericExecuteSpec tableData(String tableName);
}
