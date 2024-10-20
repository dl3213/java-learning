package code.sibyl.job

import code.sibyl.KotlinApplication
import code.sibyl.common.r
import code.sibyl.service.UpdateService
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.time.LocalDateTime


@Component
@RequiredArgsConstructor
@EnableScheduling
class Task {

    private val log = LoggerFactory.getLogger(KotlinApplication::class.java)

    //    @Async("virtualThreadTaskExecutor")
//    @Scheduled(cron = "0/10 * * * * ?")
    fun run() {
        println(Thread.currentThread().isVirtual)
        log.info(
            "测试定时任务 s: {}, {}",
            r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS),
            Thread.currentThread().name
        )
    }

    //    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
    fun runPerMin() {
        log.info("每分钟任务-start: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
//        r.sleep(70000L)
        log.info("每分钟任务-end: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
    }

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    fun runPerHour() {
        log.info("每小时任务 s: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
        UpdateService.getBean().文件补充hash().subscribe();
    }
}