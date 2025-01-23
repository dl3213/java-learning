package code.sibyl.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import code.sibyl.common.r;
import code.sibyl.config.adapter.LocalDateTimeTypeAdapter;
import code.sibyl.config.adapter.LocalDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.tika.Tika;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.resource.PathResourceResolver;
import org.springframework.web.reactive.resource.ResourceWebHandler;
import reactor.netty.http.client.HttpClient;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class CommonConfig {

    //    @Primary
    @Bean("virtualExecutor")
    public AsyncTaskExecutor virtualExecutor() {
        TaskExecutorAdapter adapter = new TaskExecutorAdapter(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("sibyl-virtual-taskExecutor-").factory()));
        return adapter;
    }


    //    @Primary
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() { // 作用于@Async + @Scheduled
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(64);
        executor.setMaxPoolSize(64);
        executor.setQueueCapacity(64 * 4);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(10);
        executor.setPrestartAllCoreThreads(true);
        //executor.setKeepAliveSeconds(1*60*60);
        executor.setThreadNamePrefix("sibyl-taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

//    @Bean
//    public ThreadPoolTaskScheduler taskScheduler() {// 作用于@Scheduled
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(64);
//        //executor.setKeepAliveSeconds(1*60*60);
//        scheduler.setThreadNamePrefix("sibyl-taskScheduler-");
//        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        return scheduler;
//    }


    @Bean
    public RouterFunction<ServerResponse> staticResources() throws MalformedURLException {
        return RouterFunctions.resources(r.staticFileBasePath, new FileSystemResource(r.fileBaseDir()), ((resource, httpHeaders) -> {
            httpHeaders.set("sibyl", "test");
            httpHeaders.setAcceptCharset(Arrays.asList(
                    Charset.forName("UTF-8"),
                    Charset.forName("GBK"),
                    Charset.forName("ISO-8859-1")
            ));
        }))
//                .and(RouterFunctions.resources("/images/**", new FileSystemResource("file:/path/to/images/")))
//                .and(RouterFunctions.resources("/other/**", new FileSystemResource("file:/another/path/to/resources/")))
                ;
    }

    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setDatabase(4);
        return (Redisson) Redisson.create(config);
    }

    @Bean
    public WebClient webClient() {
        // 创建不验证SSL的HttpClient
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)));
        // 使用HttpClient创建WebClient
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public Snowflake snowflake() {
        return IdUtil.createSnowflake(1, 1);
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }

    @Bean
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        return gsonBuilder.setPrettyPrinting().create();
    }
}
