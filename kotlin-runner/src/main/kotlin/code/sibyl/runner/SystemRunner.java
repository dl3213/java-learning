package code.sibyl.runner;

import code.sibyl.cache.LocalCacheUtil;
import code.sibyl.common.r;
import code.sibyl.config.R2dbcRoutingConfig;
import code.sibyl.repository.DatabaseRepository;
import code.sibyl.repository.eos.EosRepository;
import code.sibyl.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final FileService fileService;
    private final EosRepository eosRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("系统初始化工作--start");
        fileService.init();
        r.getBean(LocalCacheUtil.class).init();
        //r.getBean(R2dbcRoutingConfig.class).connectionFactories().doOnNext(e -> System.err.println(e)).subscribe();
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

        long start = System.currentTimeMillis();
//        r.getBean(R2dbcRoutingConfig.class)
//                .connectionFactoryMap()
//                .map(e -> DatabaseClient.create(e.get("thlease_db")))
//                .doOnSuccess(client -> {
//
//                    client.sql("SELECT * FROM th_crm_rent_out where is_del = '0'")
//                            .fetch().all()
//                            .concatWith(client.sql("SELECT * FROM th_crm_rent_out where is_del = '0'").fetch().all())
//                            .doFinally(e -> {
//                                System.err.println("cost => " + (System.currentTimeMillis() - start));
//                            })
//                            .subscribe(item -> {
//                                System.err.println(item);
//                            });
//
//                }).subscribe();
//        eosRepository.test()
//                .concatWith(eosRepository.test())
//                .doFinally(e -> System.err.println("cost => " + (System.currentTimeMillis() - start)))
//                .subscribe(json -> System.err.println(json));

        log.info("系统初始化工作--end");
    }

    @Override
    public void destroy() throws Exception {
        log.info("com.tanghe.runner.SystemRunner.destroy");
    }

}
