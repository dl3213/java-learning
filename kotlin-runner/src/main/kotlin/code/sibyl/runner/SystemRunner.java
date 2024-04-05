package code.sibyl.runner;

import code.sibyl.repository.DatabaseRepository;
import code.sibyl.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.FtpServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
/**
 * 系统启动准备
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class SystemRunner implements CommandLineRunner, DisposableBean {


    @Value("${runnerEnabled}")
    private boolean runnerEnabled;
    @Value("${isDev}")
    private boolean isDev;
    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final DatabaseRepository databaseRepository;
    private final FtpServer ftpServer;
    private final FileStorageService fileStorageService;

    @Override
    public void run(String... args) throws Exception {
        log.info("系统初始化工作--start");
        ftpServer.start();
        fileStorageService.init();
//        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
//        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
//        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
//        System.err.println(runtimeMXBean.getVmName());
// 程序运行时间
//        Instant instant = Instant.ofEpochMilli(runtimeMXBean.getStartTime());
//        System.err.println(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toString().replace("T", " "));
        // 程序已运行时间   System.currentTimeMillis(), runtimeMXBean.getStartTime()

//        System.err.println(System.currentTimeMillis() - runtimeMXBean.getStartTime());

//        System.err.println(databaseClient);
//        System.err.println(r2dbcEntityTemplate);
//        ConnectionFactory connectionFactory = databaseClient.getConnectionFactory();
//        ConnectionFactoryMetadata metadata = connectionFactory.getMetadata();
//        System.err.println(metadata.getName());
//        databaseClient.sql("select * from T_SYS_USER").fetch().all().map(m -> {
//            System.err.println(m);
//            return m;
//        }).subscribe();
        log.info("系统初始化工作--end");
    }

    @Override
    public void destroy() throws Exception {
        log.info("com.tanghe.runner.SystemRunner.destroy");
        ftpServer.stop();
    }

}
