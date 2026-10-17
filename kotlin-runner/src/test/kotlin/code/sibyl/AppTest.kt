package code.sibyl

import code.sibyl.common.r
import code.sibyl.service.BookService
import code.sibyl.service.BookService.Companion.bean
import code.sibyl.service.SteamService
import code.sibyl.service.UpdateService
import code.sibyl.service.backup.BackupService
import code.sibyl.service.sql.PostgresqlService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.kotlin.core.publisher.toFlux
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@SpringBootTest(properties = arrayOf("job.enabled=false"))
class AppTest {

    @Test
    open fun book() {
        //BookService.bean.move_test().block()
//        BookService.bean.pageNumBuild().block()
        SteamService.getBean().friendList().block()
    }

    @Test
    open fun contextLoads() {
//        UpdateService.getBean().视频文件补充thumbnail().block();
//        UpdateService.getBean().bigVideo2m3u8().block();
//        UpdateService.getBean().视频文件补充时长().block();
        UpdateService.getBean().pixiv补充pixivCode().block()
    }

    /**
     * 批量切片 >= 5GB 的视频文件(UpdateService.bigVideo2m3u8)
     * - 仅 mp4(-c copy 流复制,10 秒分片),串行执行,大文件优先
     * - 切片输出: {fileBaseDir}/cache/m3u8/{id}/{id}.m3u8,并回写 m3u8_path 到数据库
     * - 注意: E 盘为 USB 机械盘,单个大文件约需 1~2 小时;运行前确认没有其他 ffmpeg 在转换同一批文件
     */
    @Test
    open fun bigVideo2m3u8Test() {
        val count = UpdateService.getBean().bigVideo2m3u8().block()
        println("[bigVideo2m3u8Test] 完成切片 $count 个")
    }

    /**
     * 图片回填 pHash 感知哈希(近似重复检测)
     * - 全库 image 类型约 1.5 万张,CPU 预计几分钟跑完
     * - 重复执行安全:已回填的(phash 非空)自动跳过
     */
    @Test
    open fun imgPHashBackfill() {
        val count = UpdateService.getBean().图片补充phash().block()
        println("[imgPHashBackfill] 完成回填 $count 个")
    }

    /**
     * pHash 相似搜索效果演示
     * - 随机取一张已回填的图作为查询源,按汉明距离 <=12 打印 Top-10
     * - distance=0 即完全相同指纹(近似重复),越小越像
     */
    @Test
    open fun imgPHashSearchDemo() {
        val client = PostgresqlService.getBean().template().getDatabaseClient()
        val sample = client
            .sql("select id, real_name, phash from t_base_file where is_deleted='0' and type like 'image%' and phash is not null order by random() limit 1")
            .fetch().first().block() ?: return println("库中还没有已回填 phash 的图片,先跑 imgPHashBackfill")
        val id = (sample["id"] as Number).toLong()
        println("查询源: id=$id  name=${sample["real_name"]}  phash=${sample["phash"]}")
        val hits = code.sibyl.service.ImageSimilarityService.getBean()
            .searchByPhash(id, sample["phash"] as String, 12, 10).block()
        if (hits.isNullOrEmpty()) println("阈值内无相似图片")
        else {
            println("命中 ${hits.size} 张:")
            hits.forEach { println("  距离=${it["distance"]}  id=${it["id"]}  ${it["real_name"]}") }
        }
    }

    /**
     * 视频回填 vphash(视频封面帧指纹,相似视频初筛)
     * - 全库视频约 3000 个,读缩略图 jpg 计算指纹,预计几分钟
     * - 重复执行安全:只补 vphash 为空的
     */
    @Test
    // powershell -ExecutionPolicy Bypass -File C:\dl3213\4dev\workspace\java-learning\kotlin-runner\backfill-video-vphash.ps1
    // powershell -ExecutionPolicy Bypass -File C:\dl3213\4dev\workspace\java-learning\kotlin-runner\backfill-video-vhash-frames.ps1
    open fun videoVphashBackfill() {
        val count = UpdateService.getBean().视频补充vphash().block()
        println("[videoVphashBackfill] 完成回填 $count 个")
    }

    /**
     * 视频相似初筛演示
     * - 随机取一个已回填 vphash 的视频,按汉明距离 <=10 打印 Top-10
     * - distance=0 表示封面帧指纹完全一致
     * - 注意:仅输出 id/距离/大小,不打印文件名(避免 NSFW 标题进日志)
     */
    @Test
    open fun videoVphashSearchDemo() {
        val client = PostgresqlService.getBean().template().getDatabaseClient()
        val sample = client
            .sql("select id, vphash from t_base_file where is_deleted='0' and type like 'video%' and vphash is not null order by random() limit 1")
            .fetch().first().block() ?: return println("库中还没有已回填 vphash 的视频,先跑 videoVphashBackfill")
        val id = (sample["id"] as Number).toLong()
        println("查询源: id=$id  vphash=${sample["vphash"]}")
        val hits = code.sibyl.service.ImageSimilarityService.getBean()
            .searchByVphash(id, sample["vphash"] as String, 10, 10).block()
        if (hits.isNullOrEmpty()) println("阈值内无相似视频")
        else {
            println("命中 ${hits.size} 个:")
            hits.forEach { println("  距离=${it["distance"]}  id=${it["id"]}  size=${it["size"]}") }
        }
    }

    /**
     * 图片相似分组(页面"相似"选项用)
     * - 按 phash 汉明距离 ≤10 聚类,给每组写 similar_group 标签;全量重算(先清旧标签)
     * - 1 万张图约 5000 万次比对,Java 侧秒级完成
     * - 跑完后在文件页面勾选 options → 相似,列表只显示有相似伙伴的图片并按组相邻排序
     */
    @Test
    open fun imgSimilarGroupsBuild() {
        val count = UpdateService.getBean().图片补充相似分组().block()
        println("[imgSimilarGroupsBuild] 完成相似分组 $count 组")
    }

    /**
     * 视频相似分组(页面"视频相似"选项用)
     * - 判据:对应帧汉明距离 ≤8 的帧数 ≥ max(4, ceil(较短帧数×0.6))
     * - 3000 个视频约 460 万对,Java 侧约 1~3 秒;全量重算(先清旧标签)
     * - 需要 vhash 已回填(videoVhashFramesBackfill)
     */
    @Test
    open fun videoSimilarGroupsBuild() {
        val count = UpdateService.getBean().视频补充相似分组().block()
        println("[videoSimilarGroupsBuild] 完成视频相似分组 $count 组")
    }

    /**
     * 视频回填多帧指纹 vhash(方案 B:12 帧均匀抽帧 + 中心裁剪 80%)
     * - 每个视频:ffprobe 探时长 + 12 次关键帧 seek,HDD 上约 3~5 秒/个
     * - 3000 个视频预计 3~5 小时,建议夜间跑;幂等,只补 vhash 为空的
     * - 命令: powershell -ExecutionPolicy Bypass -File backfill-video-vhash-frames.ps1
     */
    @Test
    open fun videoVhashFramesBackfill() {
        val count = UpdateService.getBean().视频补充vhash().block()
        println("[videoVhashFramesBackfill] 完成回填 $count 个")
    }

    /**
     * 多帧相似视频演示(只输出 id/命中帧数/大小,不打印文件名)
     * - 相似判据:12 帧里 ≥8 帧的汉明距离 ≤8
     */
    @Test
    open fun videoVhashFramesSearchDemo() {
        val client = PostgresqlService.getBean().template().getDatabaseClient()
        val sample = client
            .sql("select id, vhash from t_base_file where is_deleted='0' and type like 'video%' and vhash is not null order by random() limit 1")
            .fetch().first().block() ?: return println("库中还没有 vhash 的视频,先跑 videoVhashFramesBackfill")
        val id = (sample["id"] as Number).toLong()
        val csv = sample["vhash"] as String
        println("查询源: id=$id  frames=${csv.split(",").size}")
        val hits = code.sibyl.service.ImageSimilarityService.getBean()
            .searchByVhashFrames(id, csv, 8, 8, 10).block()
        if (hits.isNullOrEmpty()) println("未找到相似视频(12 帧里 ≥8 帧命中)")
        else {
            println("命中 ${hits.size} 个:")
            hits.forEach { println("  命中帧=${it["hits"]}/${it["vhash_frames"]}  id=${it["id"]}  size=${it["size"]}") }
        }
    }


    @Test
    open fun pixiv_hash_clear() {
        var client = PostgresqlService.getBean().template().databaseClient

        client.sql(
            """
            select sha256 from (
                    select sha256, count(1) as count from T_BASE_FILE
                    where is_deleted = '0' 
                    and sha256 is not null
                    and code = 'pixiv'
                    group by sha256
                    )t where count >=2 
        """.trimIndent()
        )
            .fetch()
            .all()
            .flatMap {
                client.sql(
                    """
                    select * 
                    from t_base_file 
                    where is_deleted = '0' 
                    and code = 'pixiv'
                    and sha256 =:sha256
                    order by sha256 asc, create_time asc
                """.trimIndent()
                )
                    .bind("sha256", it.get("sha256") as String)
                    .fetch()
                    .all()
                    .collectList()
            }
            .flatMap {
                println(it[0]["sha256"] as String + " -> " + it.size)
                return@flatMap it.take(it.size - 1)
                    .toFlux()
                    .flatMap { item ->
                        client.sql("update t_base_file set is_deleted = '1' where id = :id ")
                            .bind("id", item.get("id") as Long)
                            .fetch()
                            .rowsUpdated()
                    }
                    .count()
            }
            .count()
            .doOnNext {
                println("共 ${it} 个")
            }
            .block()

    }

    @Test
    open fun backup() {
        BackupService.getBean().backup("sibyl", r.getBean(R2dbcEntityTemplate::class.java)).map { it -> println(it) }
            .block();
    }

    @Test
    open fun hash() {
        val file = File("")
        val sha256_1 = computeFileSHA256(file)
        println("SHA-256: $sha256_1")

        var file2byte = file2byte(file)
        var hashBytes = file2byte?.let { hashBytes(it) }
        println("SHA-256: $hashBytes")
    }

    fun computeFileSHA256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        FileInputStream(file).use { fis ->
            val buffer = ByteArray(1024)
            var numRead = 0
            while (fis.read(buffer).also { numRead = it } > 0) {
                digest.update(buffer, 0, numRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    fun file2byte(file: File): ByteArray? {
        return file.readBytes();
    }

    fun hashBytes(bytes: ByteArray): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(bytes)
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }
}
