package code.sibyl.service.backup;

import java.util.List;

public interface Metadata {

    String dbName();
    List<String> tableNameList();
}
