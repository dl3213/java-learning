package code.sibyl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ThreadConfig {

//    @Primary
//    @Bean
//    public AsyncTaskExecutor virtualThreadTaskExecutor() {
//        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
//    }

    @Primary
    @Bean
    public TaskExecutor taskExecutor() {
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
}
