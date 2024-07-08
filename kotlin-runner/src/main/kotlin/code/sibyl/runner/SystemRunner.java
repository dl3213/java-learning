package code.sibyl.runner;

import code.sibyl.common.Response;
import code.sibyl.domain.database.Database;
import code.sibyl.event.SibylEvent;
import code.sibyl.repository.DatabaseRepository;
import code.sibyl.repository.eos.EosRepository;
import code.sibyl.service.FileService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

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
    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        log.info("系统初始化工作--start");
//        fileService.init();
//        r.getBean(LocalCacheUtil.class).init();

//        databaseRepository.list_test(Arrays.asList("mysql","postgresql" ))
//                .doOnError(e -> e.printStackTrace())
//                .subscribe(item -> {
//                    System.err.println(item);
//                });

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
//        HashMap<String, Object> map = new HashMap<>();
////        map.put("sales_contract","SZ20240291");
//        map.put("sales_contract_list",Arrays.asList("SZ20240291"));
//        long start = System.currentTimeMillis();
//        r.getBean(R2dbcRoutingConfig.class)
//                .connectionFactoryMap()
//                .map(e -> DatabaseClient.create(e.get("thlease_db")))
//                .doOnSuccess(client -> {
//                    client.sql("SELECT * FROM th_crm_rent_out where is_del = '0' and sales_contract in (:{sales_contract_list})")
//                            //.bind("sales_contract", Arrays.asList("SZ20240291"))
//                            .bindValues(map)
//                            .fetch()
//                            .all()
//                            //.concatWith(client.sql("SELECT * FROM th_crm_rent_out where is_del = '0'").fetch().all())
//                            .doFinally(e -> {
//                                System.err.println("cost => " + (System.currentTimeMillis() - start));
//                            })
//                            .subscribe(item -> {
//                                System.err.println(item);
//                            });
//
//                }).subscribe();

//        databaseRepository.findBy(Example.of(new Database()), x -> x.page(PageRequest.of(0, 1))).subscribe(e -> {
//            System.err.println(e.getTotalPages());
//            Class<? extends Page> aClass = e.getClass();
//            e.get().forEach(System.err::println);
//        });

//        databaseRepository.list(PageRequest.of(0, 1)).subscribe(e -> {
//            System.err.println(e);
//            System.err.println(e.getTotalPages());
//            e.get().forEach(System.err::println);
//        });

        Query query = Query.query(Criteria.where("name").like("%lease%"));
        r2dbcEntityTemplate.select(query, Database.class).subscribe(e ->{
            System.err.println(e);
        });

//        databaseRepository.findByNameContaining("lease", PageRequest.of(0,2)).subscribe(e -> {
//            System.err.println(e.getTotalPages());
//            e.get().forEach(System.err::println);
//        });
        log.info("系统初始化工作--end");
        applicationContext.publishEvent(new SibylEvent(this,"runner-end"));
    }

    @Override
    public void destroy() throws Exception {
        log.info("code.sibyl.runner.SystemRunner.destroy");
    }

    public static void main(String[] args) {
        JSONObject data = new JSONObject();
        //data.put("test", "1");
        System.err.println(data.isEmpty());

        String key = "text2024006";
        Mono
                .justOrEmpty(data)
                //.transformDeferred(e -> Mono.just(new JSONObject()))
                .zipWhen(e -> Objects.isNull(e) || e.isEmpty() ? Mono.just(Response.error(404, "404")) : Mono.just(Response.success(200, "200")))
                //.contextWrite(context -> context.put(key,"231"))
                .transformDeferredContextual((e, contextView) -> {
                    System.err.println(Optional.of(contextView.get(key)));
                    return e;
                })
                .contextWrite(context -> context.put(key, "111"))
                .subscribe(e -> {
                    System.err.println("subscribe");
                    System.err.println(e.getT2().get("code").toString());
                });
    }

}
