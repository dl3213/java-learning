package code.sibyl.job

import code.sibyl.KotlinApplication
import code.sibyl.common.r.getBean
import code.sibyl.common.r.sleep
import code.sibyl.service.SteamService
import code.sibyl.service.UpdateService
import code.sibyl.service.backup.BackupService
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
@RequiredArgsConstructor
class Task {

    private val log = LoggerFactory.getLogger(KotlinApplication::class.java)

//    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
    fun test1() {
        log.info("[test1 -- 2000] [{}] start", Thread.currentThread().name)
        sleep(2000)
        log.info("[test1 -- 2000] [{}] end", Thread.currentThread().name)
    }

//    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
    fun test2() {
        log.info("[test2 -- 5000] [{}] start", Thread.currentThread().name)
        sleep(5000)
        log.info("[test2 -- 5000] [{}] end", Thread.currentThread().name)
    }

//    @Async
//    @Scheduled(cron = "0 0 0 * * ?")
    fun backup(){
        BackupService.getBean()
            .backup("sibyl", getBean(R2dbcEntityTemplate::class.java))
            .map { e: Long? ->
                System.err.println(e)
                e
            }
            .subscribe()
    }

    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    @ConditionalOnBean(value = [R2dbcEntityTemplate::class])
    fun 图片补充大小() {
        UpdateService.getBean().图片补充大小().subscribe()
    }

    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    @ConditionalOnBean(value = [R2dbcEntityTemplate::class])
    fun 文件补充hash() {
        UpdateService.getBean().文件补充hash().subscribe();
    }

    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    @ConditionalOnBean(value = [R2dbcEntityTemplate::class])
    fun 视频文件补充thumbnail() {
        UpdateService.getBean().视频文件补充thumbnail().subscribe();
    }

    @Async
    @Scheduled(cron = "0 0 0/2 * * ?")
    @ConditionalOnBean(value = [R2dbcEntityTemplate::class])
    fun friendList() {
        SteamService.getBean().friendList().subscribe()
    }
}