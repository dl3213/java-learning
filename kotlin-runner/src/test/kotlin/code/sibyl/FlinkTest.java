package code.sibyl;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class FlinkTest {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(new Configuration().set(RestOptions.PORT, 9090));
        env.setParallelism(1);
        //1.1Checkpoint相关
        /*读取的是binlog中的数据，如果集群挂掉，尽量能实现断点续传功能。如果从最新的读取（丢数据）。如果从最开始读（重复数据）。理想状态：读取binlog中的数据读一行，保存一次读取到的（读取到的行）位置信息。而flink中读取行位置信息保存在Checkpoint中。使用Checkpoint可以把flink中读取（按行）的位置信息保存在Checkpoint中*/
        env.enableCheckpointing(10000L);//s执行一次Checkpoint
        env.getCheckpointConfig().setCheckpointTimeout(30000L);
        //env.getCheckpointConfig().setCheckpointStorage();
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

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
        dbProps.put("sslMode", "DISABLED");
        dbProps.put("enabledTLSProtocols", "TLSv1.2");
        //通过FlinkCDC构建SourceFunction
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("127.0.0.1")
                .port(3306)
                .username("root")
                .password("test")
                .databaseList("db1")    //监控的数据库
                .tableList("db1.t_test")	//监控的数据库下的表
                .deserializer(new JsonDebeziumDeserializationSchema())//反序列化
                .debeziumProperties(dbProps)
                .startupOptions(StartupOptions.initial()) //
                .jdbcProperties(dbProps)
                .build();

        env
                .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "eos-test")
                .setParallelism(4)
                .print()
                .setParallelism(1);

        env.execute();
    }
}
