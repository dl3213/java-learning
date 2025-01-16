package code.sibyl;

import code.sibyl.common.r;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.shaded.zookeeper3.org.apache.zookeeper.Transaction;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.CatalogDescriptor;
import org.apache.flink.types.Row;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import static org.apache.flink.table.api.Expressions.$;

public class FlinkTest {

    public static void main123(String[] args) {
        System.err.println(STR."cpu core -> \{Runtime.getRuntime().availableProcessors()}");
        long start = System.currentTimeMillis();
        Flux.range(1, 10)
//                .publishOn(Schedulers.parallel())
//                .subscribeOn(Schedulers.boundedElastic())
                .parallel(8)
                .runOn(Schedulers.parallel())
                .map(e -> {
                    System.err.println(STR."\{Thread.currentThread()} -> \{e}");
                    r.sleep(1000);
                    return 1;
                })
                .reduce((a, b) -> a + b)
                .map(count -> {
                    System.err.println(STR."end, count = \{count}  cost = \{(System.currentTimeMillis() - start)}");
                    return count;
                })
//                .doFinally(_ -> System.err.println(STR."end, cost -> \{(System.currentTimeMillis() - start)}"))
//                .doOnComplete(() -> System.err.println(STR."end, cost -> \{(System.currentTimeMillis() - start)}"))
                //.subscribeOn(Schedulers.boundedElastic())
//                .log()
//                .contextWrite(context -> Context.of("total", 1))
                .subscribe();


//        Flux.range(1, 10)
//                //.repeat()
//                .parallel(8) //parallelism
//                .runOn(Schedulers.parallel())
//                .doOnNext(d -> System.out.println(STR."I'm on thread \{Thread.currentThread()} -> " + d))
//                .doOnComplete()
//                .subscribe();

        r.sleep(10000);
    }


    public static ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(32);
        executor.setMaxPoolSize(64);
        executor.setQueueCapacity(32);
        //executor.setKeepAliveSeconds(1*60*60);
        executor.setThreadNamePrefix("kotlin-runner-taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    public static void main123222(String[] args) throws Exception {
        MiniClusterConfiguration configuration = new MiniClusterConfiguration.Builder()
                .setConfiguration(new Configuration().set(RestOptions.PORT, 9090))
                .build();
        MiniCluster miniCluster = new MiniCluster(configuration);
        miniCluster.start();
    }

    //https://nightlies.apache.org/flink/flink-docs-release-1.20/docs/dev/datastream/sources/#use-the-source
    //flink DataStream API Table API
    public static void main(String[] args) throws Exception {

        //LocalStreamEnvironment localEnvironment = LocalStreamEnvironment.createLocalEnvironment();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(new Configuration());

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        String sourceTable = """
                CREATE TABLE mysql_source (
                                id BIGINT,
                                name STRING,
                                type STRING
                                ) WITH (
                                'connector' = 'jdbc',
                                'driver' = 'com.mysql.cj.jdbc.Driver',
                                'url' = 'jdbc:mysql://127.0.0.1:3306/sibyl?useSSL=false&characterEncoding=utf-8&autoReconnect=true',
                                'username' = 'root',
                                'password' = '123456',
                                'table-name' = 't_biz_book'    
                                );
                """;
        tEnv.executeSql(sourceTable);
        //tEnv.executeSql("select * from mysql_source").print();
        String sinkTable = """
                CREATE TABLE mysql_sink (
                                id BIGINT,
                                name STRING,
                                type STRING
                                ) WITH (
                                'connector' = 'jdbc',
                                'driver' = 'org.postgresql.Driver',
                                'url' = 'jdbc:postgresql://127.0.0.1:5432/postgres',
                                'username' = 'postgres',
                                'password' = '123456',
                                'table-name' = 't_biz_book'
                                );
                """;

        // jdbc 所支持的参数配置：
        //chunk-key.even-distribution.factor.lower-bound：块键（Chunk Key）的均匀分布因子下限。
        //chunk-key.even-distribution.factor.upper-bound：块键的均匀分布因子上限。
        //chunk-meta.group.size：块元数据的分组大小。
        //connect.max-retries：连接重试的最大次数。
        //connect.timeout：连接的超时时间。
        //connection.pool.size：连接池的大小。
        //connector：使用的连接器的名称。
        //database-name：数据库的名称。
        //heartbeat.interval：心跳间隔时间。
        //hostname：主机名或 IP 地址。
        //password：连接到数据库或其他系统所需的密码。
        //port：连接的端口号。
        //property-version：属性版本。
        //scan.incremental.snapshot.chunk.key-column：增量快照的块键列。
        //scan.incremental.snapshot.chunk.size：增量快照的块大小。
        //scan.incremental.snapshot.enabled：是否启用增量快照。
        //scan.newly-added-table.enabled：是否启用新加入表的扫描。
        //scan.snapshot.fetch.size：从状态快照中获取的每次批量记录数。
        //scan.startup.mode：扫描启动模式。
        //scan.startup.specific-offset.file：指定启动位置的文件名。
        //scan.startup.specific-offset.gtid-set：指定启动位置的 GTID 集合。
        //scan.startup.specific-offset.pos：指定启动位置的二进制日志位置。
        //scan.startup.specific-offset.skip-events：跳过的事件数量。
        //scan.startup.specific-offset.skip-rows：跳过的行数。
        //scan.startup.timestamp-millis：指定启动时间戳（毫秒）。
        //server-id：服务器 ID。
        //server-time-zone：服务器时区。
        //split-key.even-distribution.factor.lower-bound：切分键（Split Key）的均匀分布因子下限。
        //split-key.even-distribution.factor.upper-bound：切分键的均匀分布因子上限。
        //table-name：表名。
        //username：连接到数据库或其他系统所需的用户名。
        //Sink目标表with下的属性：
        //connection.max-retry-timeout：连接重试的最大超时时间。
        //connector：使用的连接器的名称。
        //driver：JDBC 连接器中使用的数据库驱动程序的类名。
        //lookup.cache：查找表的缓存配置。
        //lookup.cache.caching-missing-key：是否缓存查找表中的缺失键。
        //lookup.cache.max-rows：查找表缓存中允许的最大行数。
        //lookup.cache.ttl：查找表缓存中行的生存时间。
        //lookup.max-retries：查找操作的最大重试次数。
        //lookup.partial-cache.cache-missing-key：是否缓存查找表部分缺失的键。
        //lookup.partial-cache.expire-after-access：查找表部分缓存中行的访问到期时间。
        //lookup.partial-cache.expire-after-write：查找表部分缓存中行的写入到期时间。
        //lookup.partial-cache.max-rows：查找表部分缓存中允许的最大行数。
        //password：连接到数据库或其他系统所需的密码。
        //property-version：属性版本。
        //scan.auto-commit：是否自动提交扫描操作。
        //scan.fetch-size：每次批量获取记录的大小。
        //scan.partition.column：用于分区的列名。
        //scan.partition.lower-bound：分区的下限值。
        //scan.partition.num：要扫描的分区数量。
        //scan.partition.upper-bound：分区的上限值。
        //sink.buffer-flush.interval：将缓冲区的数据刷新到目标系统的时间间隔。
        //sink.buffer-flush.max-rows：缓冲区中的最大行数，达到此值时将刷新数据。
        //sink.max-retries：写入操作的最大重试次数。
        //sink.parallelism：写入任务的并行度。
        //table-name：表名。
        //url：连接到数据库或其他系统的 URL。
        //username：连接到数据库或其他系统所需的用户名。
        tEnv.executeSql(sinkTable);
        tEnv.executeSql("insert into mysql_sink select id,name,type from mysql_source");
        System.out.println("MySQL to MySQL ");

    }

}
