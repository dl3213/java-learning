package code.sibyl.job

import code.sibyl.KotlinApplication
import code.sibyl.common.r
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Date


@Component
@RequiredArgsConstructor
@EnableScheduling
class Task {

    private val log = LoggerFactory.getLogger(KotlinApplication::class.java)

    @Scheduled(cron = "0 0/1 * * * ?")
    fun runPerMin() {
        log.info("每分钟任务: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    fun runPerHour() {
        log.info("每小时任务: {}", r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS))
    }
}