package code.sibyl.service.backup;

import cn.hutool.core.compress.ZipWriter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import code.sibyl.common.r;
import code.sibyl.service.backup.MetadataBuilder;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(value = R2dbcEntityTemplate.class)
public class BackupService {

    public static BackupService getBean() {
        return r.getBean(BackupService.class);
    }

    public Mono<Long> backup(String dbName, R2dbcEntityTemplate r2dbcEntityTemplate) {
        return backup(dbName, r2dbcEntityTemplate.getDatabaseClient());
    }

    // todo ddl
    public Mono<Long> backup(String dbName, DatabaseClient databaseClient) {
        long start = System.currentTimeMillis();
        ConnectionFactory connectionFactory = databaseClient.getConnectionFactory();
        String name = connectionFactory.getMetadata().getName();
        log.info("[BackupService.backup] {}", STR."\{name} -> \{dbName}");
        String absolutePath = "E:\\sibyl-system\\"; // r.absolutePath();
        log.info("[BackupService.backup] {}", STR."ApplicationHome -> \{absolutePath}");

        final String backupPath = STR."\{absolutePath}\{File.separator}backup\{File.separator}\{dbName}\{File.separator}\{r.yyyy_MM_dd()}\{File.separator}";
        log.info("[BackupService.backup] {}", STR."backupPath -> \{backupPath}");
        File fileDir = new File(backupPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        final String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern(r.yyyy_MM_dd));
        return MetadataBuilder.build(dbName, name, databaseClient)
                .flatMapMany(item -> item.tableNameList().flatMap(tableName -> Mono.zip(Mono.just(item), Mono.just(tableName))))
                .flatMap(tuple2 -> {
                    String tableName = tuple2.getT2();
                    log.info("[BackupService.backup] tableName = {}", tableName);
                    final String backupTableSqlFile = STR."\{backupPath}\{tableName}-\{formatted}.sql";
                    final String backupTableJsonFile = STR."\{backupPath}\{tableName}-\{formatted}.json";
                    //log.info("[BackupService.backup] backupTableSqlFile = {}", backupTableSqlFile);
                    //log.info("[BackupService.backup] backupTableJsonFile = {}", backupTableJsonFile);
                    File tableSqlFile = new File(backupTableSqlFile);
                    File tableJsonFile = new File(backupTableJsonFile);

                    try {
                        if (tableSqlFile.exists()) {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupTableSqlFile, false))) {
                                // 清空文件内容
                                writer.write("");
                                writer.flush();
                            }
                        }
                        if (!tableSqlFile.exists()) {
                            tableSqlFile.createNewFile();
                        }
                        if (tableJsonFile.exists()) {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableJsonFile, false))) {
                                // 清空文件内容
                                writer.write("");
                                writer.flush();
                            }
                        }
                        if (!tableJsonFile.exists()) {
                            tableJsonFile.createNewFile();
                        }

                    } catch (Exception e) {
                        return Flux.error(e);
                    }
                    FileWriter sqlWriter;
                    FileWriter jsonWriter;
                    try {
                        sqlWriter = new FileWriter(tableSqlFile, true);
                        jsonWriter = new FileWriter(tableJsonFile, true); // todo 如何关闭?
                    } catch (IOException e) {
                        return Flux.error(new RuntimeException(e));
                    }

                    return tuple2.getT1().tableData(tableName)
                            .fetch()
                            .all()
                            .onErrorComplete(throwable -> {
                                System.err.println("get tableData error --> ");
                                throwable.printStackTrace();
                                return true;
                            })
                            // todo 改成批量
                            .map(data -> {
                                {

                                    try {
                                        sqlWriter.write(r.dataToInsertSql(tableName, data));
                                    } catch (IOException e) {
                                        System.err.println(STR."err ==> \{tableName} - \{data} -> \{tableSqlFile.getAbsolutePath()}");
                                        e.printStackTrace();
                                    }
                                    try {
                                        jsonWriter.write(JSONObject.toJSONString(data) + "\n");
                                    } catch (IOException e) {
                                        System.err.println(STR."err ==> \{tableName} - \{data} -> \{tableJsonFile.getAbsolutePath()}");
                                        e.printStackTrace();
                                    }
                                }

                                return data;
                            })
                            .doOnError(throwable -> {
                                System.err.println("throwable in Writer.write " + tableName);
                                throwable.printStackTrace();
                            })
                            .count()
                            .doAfterTerminate(() -> {
                                try {
                                    sqlWriter.flush();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    sqlWriter.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    jsonWriter.flush();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    jsonWriter.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                })
                .count()
                .map(count -> {
                    //压缩文件 backupPath
                    // 压缩文件保存的路径
                    File zipped = ZipUtil.zip(backupPath);
                    boolean deleted = FileUtil.del(backupPath);
                    log.info("[BackupService.backup]backup end: table count = {}, cost = {}, zipped = {}, deleted = {}", count, (System.currentTimeMillis() - start), zipped.getAbsolutePath(), deleted);
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
