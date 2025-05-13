package code.sibyl.service.backup;

import code.sibyl.common.r;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BackupService {

    public static BackupService getBean() {
        return r.getBean(BackupService.class);
    }

    public Mono<Long> backup(String dbName, R2dbcEntityTemplate r2dbcEntityTemplate) {
        return backup(dbName, r2dbcEntityTemplate.getDatabaseClient());
    }

    // todo ddl
    public Mono<Long> backup(String dbName, DatabaseClient databaseClient) {
        ConnectionFactory connectionFactory = databaseClient.getConnectionFactory();
        String name = connectionFactory.getMetadata().getName();
        log.info("[BackupService.backup]: {}", STR."\{name} -> \{dbName}");
        String absolutePath = r.absolutePath();
        log.info("[BackupService.backup]: {}", STR."ApplicationHome -> \{absolutePath}");
        String classPath = ClassLoader.getSystemResource("").getPath();
        log.info("[BackupService.backup]: {}", STR."classPath -> \{absolutePath}");
        String projectPath = new File("").getAbsolutePath();
        log.info("[BackupService.backup]: {}", STR."projectPath -> \{absolutePath}");

        final String backupPath = STR."\{absolutePath}\{File.separator}backup\{File.separator}\{dbName}\{File.separator}\{r.yyyy_MM_dd()}\{File.separator}";
        log.info("[BackupService.backup]: {}", STR."backupPath -> \{backupPath}");
        File fileDir = new File(backupPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        final String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern(r.yyyy_MM_dd));
        return MetadataBuilder.build(dbName, name, databaseClient)
                .flatMapMany(item -> item.tableNameList().flatMap(tableName -> Mono.zip(Mono.just(item), Mono.just(tableName))))
                .flatMap(tuple2 -> {
                    String tableName = tuple2.getT2();
                    log.info("[BackupService.backup]: tableName = {}", tableName);
                    final String backupTableSqlFile = STR."\{backupPath}\{tableName}-\{formatted}.sql";
                    final String backupTableJsonFile = STR."\{backupPath}\{tableName}-\{formatted}.json";
                    log.info("[BackupService.backup]: backupTableSqlFile = {}", backupTableSqlFile);
                    log.info("[BackupService.backup]: backupTableJsonFile = {}", backupTableJsonFile);
                    File tableSqlFile = new File(backupTableSqlFile);
                    File tableJsonFile = new File(backupTableJsonFile);

                    try {
                        if (tableSqlFile.exists()) {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupTableSqlFile, false))) {
                                // 清空文件内容
                                writer.write("");
                            }
                        }
                        if (!tableSqlFile.exists()) {
                            tableSqlFile.createNewFile();
                        }
                        if (tableJsonFile.exists()) {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableJsonFile, false))) {
                                // 清空文件内容
                                writer.write("");
                            }
                        }
                        if (!tableJsonFile.exists()) {
                            tableJsonFile.createNewFile();
                        }

                    } catch (Exception e) {
                        return Flux.error(e);
                    }
                    return tuple2.getT1().tableData(tableName).fetch().all().flatMap(data -> Mono.zip(Mono.just(tableName), Mono.just(data), Mono.just(tableSqlFile), Mono.just(tableJsonFile)));
                })
                .map(item -> {
                    String tableName = item.getT1();
                    Map<String, Object> data = item.getT2();
                    try {
                        File tabelSqlFile = item.getT3();
                        try (FileWriter writer = new FileWriter(tabelSqlFile, true)) {
                            writer.write(r.dataToInsertSql(tableName, data));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        File tabelJsonFile = item.getT4();
                        try (FileWriter writer = new FileWriter(tabelJsonFile, true)) {
                            writer.write(JSONObject.toJSONString(data, JSONWriter.Feature.WriteMapNullValue));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return item;
                })
                .count()
                .map(count -> {
                    log.info("[BackupService.backup]: {}", STR."backup end: count -> \{count}");
                    return count;
                })
                .then()
                .thenReturn(1L)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.just(0L);
                });

    }

}
