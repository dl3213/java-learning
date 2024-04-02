package code.sibyl.job

import code.sibyl.KotlinApplication
import code.sibyl.common.r
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
@RequiredArgsConstructor
@EnableScheduling
class Task {

    private val log = LoggerFactory.getLogger(KotlinApplication::class.java)

//    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
    fun runPerMin() {
        log.info("每分钟任务-start: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
//        r.sleep(70000L)
        log.info("每分钟任务-end: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
    }

//    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    fun runPerHour() {
        log.info("每小时任务 s: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
    }
}