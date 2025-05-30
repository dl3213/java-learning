package code.sibyl.runner;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.event.Event;
import code.sibyl.repository.DatabaseRepository;
import code.sibyl.repository.eos.EosRepository;
import code.sibyl.service.FileService;
import code.sibyl.service.SteamService;
import code.sibyl.service.UpdateService;
import code.sibyl.service.backup.BackupService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
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
    private final ChatClient chatClient;
    private final org.springframework.ai.ollama.OllamaChatModel ollamaChatModel;

    @Override
    public void run(String... args) throws Exception {
        log.info("系统初始化工作--start");
        BackupService.getBean().backup("sibyl", r.getBean(R2dbcEntityTemplate.class, "sibyl-mysql")).subscribe();
//        BackupService.getBean().backup("postgres", r.getBean(R2dbcEntityTemplate.class, "sibyl-postgresql")).subscribe();
//        BookService.getBean().move_test().subscribe();
        UpdateService.getBean().file_clear().subscribe(); //
//        UpdateService.getBean().book_clear().subscribe(); //

        SteamService.getBean().friendList().subscribe();

        log.info("系统初始化工作--end");
        applicationContext.publishEvent(new Event(this, "runner-end"));
    }

    @Override
    public void destroy() throws Exception {
        log.info("code.sibyl.runner.SystemRunner.destroy");
        applicationContext.publishEvent(new Event(this, "system-destroy"));
    }

    public static void main(String[] args) {

        //
        List<Integer> list = Arrays.asList(1, 2, 3);
        for (Integer i : list) {
            // loop
        }

        JSONObject data = new JSONObject();
        //data.put("test", "1");
        System.err.println(data.isEmpty());

        String key = "text2024006";
        Mono.justOrEmpty(data)
                //.transformDeferred(e -> Mono.just(new JSONObject()))
                .zipWhen(e -> Objects.isNull(e) || e.isEmpty() ? Mono.just(Response.error(404, "404")) : Mono.just(Response.success(200, "200")))
                //.contextWrite(context -> context.put(key,"231"))
                .transformDeferredContextual((e, contextView) -> {
                    System.err.println(Optional.of(contextView.get(key)));
                    return e;
                }).contextWrite(context -> context.put(key, "111")).subscribe(e -> {
                    System.err.println("subscribe");
                    System.err.println(e.getT2().get("code").toString());
                });
    }

}
