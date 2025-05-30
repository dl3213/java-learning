package code.sibyl.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import code.sibyl.common.r;
import code.sibyl.config.adapter.LocalDateTimeTypeAdapter;
import code.sibyl.config.adapter.LocalDateTypeAdapter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.resource.PathResourceResolver;
import org.springframework.web.reactive.resource.ResourceWebHandler;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.netty.http.client.HttpClient;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
//@EnableWebFlux
@EnableAsync
@EnableScheduling
@Slf4j
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
    public RouterFunction<ServerResponse> sibylResources() throws MalformedURLException {
        return RouterFunctions.resources("/sibyl-file/**", new UrlResource("ftp://13.60.20.165:21"), ((resource, httpHeaders) -> {
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
                .setAddress("redis://" + r.getBean(Environment.class).getProperty("spring.data.redis.host") + ":6379")
                .setDatabase(4)
                .setConnectionPoolSize(32)
        ;
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

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("i18n/message");
//        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


    @Primary
    @Bean
    public LocaleContextResolver localeResolver() {
        AcceptHeaderLocaleContextResolver resolver = new AcceptHeaderLocaleContextResolver();
        resolver.setDefaultLocale(Locale.US); // 设置默认区域
        return resolver;
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        ChatClient chatClient = ChatClient.builder(chatModel)
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                // 实现 Logger 的 Advisor
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        OllamaOptions.builder()
                                .topP(0.7)
                                .build()
                )
                .build();
        log.info("chatClient = {}", chatClient.getClass());
        return chatClient;
    }


    public static final String cacheName_30MINUTES = "Caffeine-CACHE-30MINUTES";

    @Primary
    @Bean(name = "localEntityCacheManager")
    public CacheManager localEntityCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                .evictionListener((key, value, removalCause) -> log.info("[cache-eviction]{} - {} - {} ", key, value, removalCause))
                .removalListener((key, value, removalCause) -> log.info("[cache-removal]{} - {} - {} ", key, value, removalCause))
                .maximumSize(1024)     // 最大容量
                .expireAfterWrite(30, TimeUnit.MINUTES)     //
                .expireAfterAccess(30, TimeUnit.MINUTES);  // 分钟不访问自动丢弃
//              .executor(ThreadPoolUtil.getThreadPool()); // 走线程池，需自定义线程池,可不用
        caffeineCacheManager.setCaffeine(caffeine);
        caffeineCacheManager.setCacheNames(Arrays.asList(cacheName_30MINUTES));  // 设定缓存器名称
        caffeineCacheManager.setAllowNullValues(true);// 值不可为空\
        //caffeineCacheManager.setAsyncCacheMode(true);
        return caffeineCacheManager;
    }
}
