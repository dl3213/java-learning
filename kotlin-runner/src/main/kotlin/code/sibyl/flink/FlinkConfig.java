package code.sibyl.flink;

import code.sibyl.common.r;
import com.ververica.cdc.connectors.base.source.jdbc.JdbcIncrementalSource;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.connectors.postgres.source.PostgresSourceBuilder;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.runtime.state.storage.FileSystemCheckpointStorage;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.catalog.ObjectPath;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.Duration;
import java.util.Properties;

@ConditionalOnProperty(name = "flink.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
@RequiredArgsConstructor
@Slf4j
public class FlinkConfig {

    @Bean
    public MiniCluster miniCluster() throws Exception {
        MiniClusterConfiguration configuration = new MiniClusterConfiguration.Builder()
                .setConfiguration(new org.apache.flink.configuration.Configuration().set(RestOptions.PORT, 9090))
                .build();
        MiniCluster miniCluster = new MiniCluster(configuration);
        miniCluster.start();
        //miniCluster.close();
        //miniCluster.isRunning();
        log.info("flink MiniCluster start");
        return miniCluster;
    }


    @Bean
    public StreamExecutionEnvironment cdc() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new org.apache.flink.configuration.Configuration().set(RestOptions.PORT, 9091));
        env.setParallelism(1);
        //1.1Checkpoint相关
        /*读取的是binlog中的数据，如果集群挂掉，尽量能实现断点续传功能。如果从最新的读取（丢数据）。如果从最开始读（重复数据）。理想状态：读取binlog中的数据读一行，保存一次读取到的（读取到的行）位置信息。而flink中读取行位置信息保存在Checkpoint中。使用Checkpoint可以把flink中读取（按行）的位置信息保存在Checkpoint中*/
        env.enableCheckpointing(1000L * 60);//执行一次Checkpoint
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointTimeout(30000L);
        //env.getCheckpointConfig().setCheckpointStorage();
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE); //配置检查点模式：EXACTLY_ONCE：精确一次,AT_LEAST_ONCE:至少一次
        checkpointConfig.setMaxConcurrentCheckpoints(1);
        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.getCheckpointConfig().setCheckpointStorage(new FileSystemCheckpointStorage(Path.fromLocalFile(new File(r.absolutePath() + File.separator + "flink-checkpoint"))));
        //env.getCheckpointConfig().setCheckpointStorage(new JobManagerCheckpointStorage());
//// 检查点配置超时时间
//        env.getCheckpointConfig().setAlignmentTimeout(Duration.ofMillis(1000L));
//// 前后2个检查点之间的最小间隔
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000L);
//// 最大并发的检查点数量
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//// 开启不对齐分界线检查点保存，要求EXACTLY_ONCE精确一次，且检查点最大并发数量为1
//        env.getCheckpointConfig().enableUnalignedCheckpoints();
//// RETAIN_ON_CANCELLATION：任务取消保存检查点存储，DELETE_ON_CANCELLATION：任务取消删除检查点存储
//        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//// 是否运行检查点失败：0完全不允许失败
//        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(0);


        //设置Checkpoint的模式：精准一次
        //env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //任务挂掉的时候是否清理checkpoint。使任务正常退出时不删除CK内容，有助于任务恢复。默认的是取消的时候清空checkpoint中的数据。RETAIN_ON_CANCELLATION表示取消任务的时候，保存最后一次的checkpoint。便于任务的重启和恢复，正常情况下都使用RETAIN
        //env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //设置一个重启策略：默认的固定延时重启次数，重启的次数是Integer的最大值，重启的间隔是1s
        //env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000L));
        //设置一个状态后端 jobManager。如果使用的yarn集群模式，jobManager随着任务的生成而生成，任务挂了jobManager就没了。因此需要启动一个状态后端。只要设置checkpoint，尽量就设置一个状态后端。保存在各个节点都能读取的位置：hdfs中
        //env.setStateBackend(new FsStateBackend("hdfs://hadoop102:8020/flink/ck/"));
        //指定用户
        //System.setProperty("HADOOP_USER_NAME", "sibyl");


        Properties dbProps = new Properties();
        dbProps.put("jdbc.properties.useSSL", false);
        dbProps.put("useSSL", false);
        dbProps.put("allowPublicKeyRetrieval", true);
        dbProps.put("sslMode", "DISABLED");
        dbProps.put("enabledTLSProtocols", "TLSv1.2");

        //通过FlinkCDC构建SourceFunction

        MySqlSource<String> local_mysql = MySqlSource.<String>builder()
                .hostname("127.0.0.1")
                .port(3306)
                .username("root")
                .password("sibyl-mysql-0127")
                .databaseList("sibyl")    //监控的数据库
//                .tableList("thlease_db.th_mater_store_info, thlease_db.th_crm_rent_out,  thlease_db.th_crm_customer_info")    //监控的数据库下的表
                .tableList("sibyl.*")    //监控的数据库下的表
                .scanNewlyAddedTableEnabled(true) // 动态加表
                .deserializer(new JsonDebeziumDeserializationSchema())//反序列化
                .debeziumProperties(dbProps)
                .startupOptions(StartupOptions.latest()) //
                .jdbcProperties(dbProps)
                .build();

        Properties properties = new Properties();
        properties.setProperty("snapshot.mode", "never");
        properties.setProperty("debezium.slot.name", "pg_cdc");
        properties.setProperty("debezium.slot.drop.on.stop", "true");
        properties.setProperty("include.schema.changes", "true");
        //使用连接器配置属性启用定期心跳记录生成
        //properties.setProperty("heartbeat.interval.ms", String.valueOf(DEFAULT_HEARTBEAT_MS));


        JdbcIncrementalSource<String> local_postgres = PostgresSourceBuilder.PostgresIncrementalSource.<String>builder()
                .hostname("127.0.0.1")
                .port(5432)
                .database("postgres")
                .schemaList("public")
                .tableList("public.*")
                .username("postgres")
                .password("sibyl-postgres-0127")
                //.slotName("flink")
                .decodingPluginName("pgoutput") // use pgoutput for PostgreSQL 10+ //   ALTER TABLE t_biz_book REPLICA IDENTITY FULL; -- 执行这个之后update 才有before
                .deserializer(new JsonDebeziumDeserializationSchema())
                .debeziumProperties(properties)
                .includeSchemaChanges(true) // output the schema changes as well
                .splitSize(2) // the split size of each snapshot split
                .startupOptions(com.ververica.cdc.connectors.base.options.StartupOptions.latest()) //
                .build();

        env.fromSource(local_mysql, WatermarkStrategy.noWatermarks(), "local_mysql")
                .setParallelism(1)
                //.print()
                .addSink(new PrintRichSink())
//                .sinkTo(new FluxSink())
        ;

        env.fromSource(local_postgres, WatermarkStrategy.noWatermarks(), "local_postgres")
                .setParallelism(1)
                .addSink(new PrintRichSink());

        env.executeAsync();
        log.info("flink-cdc start");
        return env;
    }
}
