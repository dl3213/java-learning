package code.sibyl.config;

import code.sibyl.common.r;
import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.MalformedURLException;
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
        return RouterFunctions.resources("/pixiv/**", new FileSystemResource(r.fileBaseDir))
//                .and(RouterFunctions.resources("/images/**", new FileSystemResource("file:/path/to/images/")))
//                .and(RouterFunctions.resources("/other/**", new FileSystemResource("file:/another/path/to/resources/")))
                ;
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }
}
