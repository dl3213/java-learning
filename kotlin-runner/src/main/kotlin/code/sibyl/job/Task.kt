package code.sibyl.job

import code.sibyl.KotlinApplication
import code.sibyl.common.r
import code.sibyl.common.r.sleep
import code.sibyl.service.UpdateService
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
@RequiredArgsConstructor
class Task {

    private val log = LoggerFactory.getLogger(KotlinApplication::class.java)

    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    fun test1() {
        log.info("[test1 -- 2000] [{}] start", Thread.currentThread().name)
        sleep(2000)
        log.info("[test1 -- 2000] [{}] end", Thread.currentThread().name)
    }

    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    fun test2() {
        log.info("[test2 -- 5000] [{}] start", Thread.currentThread().name)
        sleep(5000)
        log.info("[test2 -- 5000] [{}] end", Thread.currentThread().name)
    }

    @Async
    @Scheduled(cron = "0 0/2 * * * ?")
    fun 图片补充大小() {
        log.info(
            "{} - 图片补充大小 : {}",
            Thread.currentThread().name,
            r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS)
        )
        UpdateService.getBean().图片补充大小().subscribe()
    }

    @Async
    @Scheduled(cron = "0 0/2 * * * ?")
    fun 文件补充hash() {
        log.info(
            "{} - 文件补充hash : {}",
            Thread.currentThread().name,
            r.formatDate(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS)
        )
        UpdateService.getBean().文件补充hash().subscribe();
    }
}