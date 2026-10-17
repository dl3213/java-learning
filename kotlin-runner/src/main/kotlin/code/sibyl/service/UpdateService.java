package code.sibyl.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.domain.biz.Book;
import code.sibyl.service.cdc.BaseFileHandler;
import code.sibyl.service.sql.PostgresqlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(value = R2dbcEntityTemplate.class)
public class UpdateService {

    public static UpdateService getBean() {
        return r.getBean(UpdateService.class);
    }


    public Mono<Long> book_clear() {
        //Path root = Path.of(r.fileBaseDir);
        Criteria criteria = Criteria.where("IS_DELETED").is("1");
        return PostgresqlService.getBean().template().getDatabaseClient().sql("select * from t_biz_book where is_deleted = '1'")
                .mapProperties(Book.class)
                .all()
                .flatMap(e -> {
                    log.info("[book_clear] file = {} ", e.getAbsolutePath());
                    boolean deleted = FileUtil.del(e.getAbsolutePath());
                    log.info("[book_clear] file = {} , deleted = {} ", e.getAbsolutePath(), deleted);
                    return PostgresqlService.getBean().template().delete(e);
//                    return Mono.just(e);
                })
                .count()
                .map(e -> {
                    log.info("[book_clear] count = {}", e);
                    return e;
                });
    }

    public Mono<Long> file_clear() {
        //Path root = Path.of(r.fileBaseDir);
        Criteria criteria = Criteria.where("IS_DELETED").is("1");
        return PostgresqlService.getBean().template().select(Query.query(criteria), BaseFile.class)
                .flatMap(e -> {
                    log.info("[file_clear] file[{}] = {} ", e.getFileName(), e.getAbsolutePath());
                    File file = new File(e.getAbsolutePath());
                    try {
                        file.delete();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return PostgresqlService.getBean().template().delete(e);
                })
                .doOnError(throwable -> {
                    log.error("[file_clear] error -> {}", throwable.getMessage());
                    throwable.printStackTrace();
                })
                .count()
                .map(e -> {
                    log.info("[file_clear] count = {}", e);
                    return e;
                });
    }

    //pixiv_init_v1 -> 22551 cost = 19851
    //pixiv_init_v1 -> 22551 cost = 23000
    //pixiv_init_v1 -> 22551 cost = 21940
    //pixiv_init_v1 -> 22551 cost = 19252
    //pixiv_init_v1 -> 22551 cost = 22528
    //pixiv_init_v1 -> 22551 cost = 19443
    //pixiv_init_v1 -> 22551 cost = 20259
    // 20241122 pixiv_init_v1 -> 22816 cost = 21618
    public Mono<Long> pixiv_init() {
        long start = System.currentTimeMillis();
        Path root = Path.of(r.fileBaseDir());
        return PostgresqlService.getBean().template().getDatabaseClient()
                .sql("select count(1) as count from T_BASE_FILE where 1=1")
                .fetch()
                .first()
                .flatMap(map -> {
                    Long count = (Long) map.get("count");
                    System.err.println(STR."当前存在文件 \{count} 个");
                    return count == 0 ? Mono.just(count) : Mono.empty();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(_ -> Flux.create((sink) -> {
                            try (Stream<Path> files = Files.walk(root)) {
                                files.forEach(e -> sink.next(e));
                                sink.complete();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                sink.error(exception);
                            }
                        })
                )
                .filter(item -> {
                    Path path = (Path) item;
                    File realFile = path.toFile();
                    return realFile.isFile() && !realFile.isDirectory();
                })
                .flatMap(item -> {
                    try {
                        Path path = (Path) item;
                        String fileName = path.getFileName().toString();
                        BaseFile file = this.file(fileName, path.toAbsolutePath().toString(), "pixiv", null, false);
                        System.err.println(STR."\{Thread.currentThread()} -> \{file.getAbsolutePath()}");
                        return PostgresqlService.getBean().template().insert(file);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return Mono.empty();
                    }
                })
                .count()
                .map(count -> {
                    System.err.println(STR."pixiv_init_v1 -> \{count} cost = \{((System.currentTimeMillis() - start))}"); //   / 1000 / 60
                    return count;
                });
    }

    //pixiv_init_parallel -> 22551 cost = 9043
    //pixiv_init_parallel -> 22551 cost = 7310
    //pixiv_init_parallel -> 22551 cost = 8240
    //pixiv_init_parallel -> 22551 cost = 12777
    //pixiv_init_parallel -> 22551 cost = 8434
    // 20241122 pixiv_init_parallel -> 22816 cost = 9093
    public Mono<Long> pixiv_init_parallel() {
        long start = System.currentTimeMillis();
        Path root = Path.of(r.fileBaseDir());
        Scheduler scheduler = Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class));
        return PostgresqlService.getBean().template().getDatabaseClient()
                .sql("select count(1) as count from T_BASE_FILE where 1=1")
                .fetch()
                .first()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .flatMap(map -> {
                    Long count = (Long) map.get("count");
                    System.err.println(STR."当前存在文件 \{count} 个");
                    return count == 0 ? Mono.just(count) : Mono.empty();
                })
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                //.subscribeOn(scheduler)
                .flatMapMany(_ -> Flux.create((sink) -> {
                            try (Stream<Path> files = Files.walk(root)) {
                                files.forEach(e -> sink.next(e));
                                sink.complete();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                sink.error(exception);
                            }
                        })
                        //.publishOn(Schedulers.fromExecutor(executor))
                )
                //.subscribeOn(Schedulers.fromExecutor(executor))
                .parallel(32)
                .runOn(Schedulers.parallel())
                .filter(item -> {
                    Path path = (Path) item;
                    File realFile = path.toFile();
                    return realFile.isFile() && !realFile.isDirectory();
                })
                .flatMap(item -> {
                    try {
                        Path path = (Path) item;
                        String fileName = path.getFileName().toString();
                        BaseFile file = this.file(fileName, path.toAbsolutePath().toString(), "pixiv", null, false);
                        //System.err.println(STR."PoolSize=\{scheduler.getPoolSize()}, activeCount=\{executor.getActiveCount()}, queueSize=\{executor.getQueueSize()}, thread=\{Thread.currentThread()}, file=\{file.getAbsolutePath()}");
                        //System.err.println(STR."thread=\{Thread.currentThread()}, file=\{file.getAbsolutePath()}");
                        return PostgresqlService.getBean().template().insert(file).map(_ -> 1L).onErrorReturn(0L);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return Mono.empty();
                    }
                })
                .reduce((a, b) -> a + b)
                .map(count -> {
                    System.err.println(STR."pixiv_init_parallel -> \{count} cost = \{((System.currentTimeMillis() - start))}"); //  1000 / 60
                    return count;
                });
    }

    public BaseFile file(String fileName, @NotNull String absolutePath, String code, byte[] bytes, boolean needCreateFile) {
        absolutePath = absolutePath.replace("\\", "/");
        File realFile = new File(absolutePath);
        BaseFile baseFile = new BaseFile();
        baseFile.setFileName(realFile.getName());
        baseFile.setRealName(fileName);
        if (Objects.nonNull(bytes) && needCreateFile) {
            //String hash = r.hashBytes(bytes); // 导致线程越来越慢
            baseFile.setType(r.getBean(Tika.class).detect(bytes));
            //baseFile.setSha256(hash);
        } else {
            try {
                baseFile.setType(r.getBean(Tika.class).detect(realFile));
                //baseFile.setSha256(r.computeFileSHA256(realFile)); // 导致线程越来越慢
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        baseFile.setAbsolutePath(absolutePath);
        baseFile.setRelativePath(absolutePath.replace(r.fileBaseDir(), ""));

        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        baseFile.setSuffix(suffix);
        if(StringUtils.isBlank(code)){
            code = "default";
        }
        baseFile.setCode(code);
        baseFile.setSerialNumber(null);
        baseFile.setDeleted("0");
        baseFile.setCreateId(r.defaultUserId());
        baseFile.setCreateTime(LocalDateTime.now());

        File parentFile = realFile.getParentFile();
        try {
            if (needCreateFile) {
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!realFile.exists()) {
                    realFile.createNewFile();
                }
                try (OutputStream outputStream = new FileOutputStream(realFile)) {
                    IOUtils.write(bytes, outputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseFile.setSize(Objects.nonNull(bytes) ? bytes.length : realFile.length());

        return baseFile;
    }

    @NotNull
    public Mono<Long> 文件补充hash() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and SHA256 IS NULL limit 500")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .flatMap(item -> BaseFileHandler.getBean().sha256(item))
                .count()
                .map(count -> {
//                    log.info("[文件补充hash] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    @NotNull
    public Mono<Long> 图片补充大小() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where type like 'image%' and (WIDTH is null or HEIGHT is null) limit 100")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .flatMap(item -> BaseFileHandler.getBean().图片补充大小(item))
                .count()
                .map(count -> {
//                    log.info("[图片补充大小] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    @NotNull
    public Mono<Long> 视频文件补充thumbnail() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and type like 'video%' and (thumbnail is null) limit 100")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .flatMap(item -> BaseFileHandler.getBean().视频文件补充thumbnail(item))
                .count()
                .map(count -> {
//                    log.info("[视频文件补充thumbnail] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    @NotNull
    public Mono<Long> 视频文件补充时长() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and type like 'video%' and (video_duration_second is null) limit 100")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .flatMap(item -> BaseFileHandler.getBean().视频文件补充时长(item))
                .count()
                .map(count -> {
//                    log.info("[视频文件补充时长] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }


    @NotNull
    public Mono<Long> bigVideo2m3u8() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        // >= 5GB 的 mp4 视频(5*1024^3=5368709120);wmv 等非 H.264 容器流复制出来的 HLS 浏览器无法解码,跳过
        // concatMap 串行转换:避免多个 ffmpeg 同时读写 USB 机械盘导致 IO 争抢;size desc 大文件优先
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and type like 'video%' and suffix = 'mp4' and size >= 5368709120 and (m3u8_path is null) order by size desc")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .concatMap(entity -> {
                    String absolutePath = entity.getAbsolutePath();
                    String m3u8Path = File.separator + "m3u8" + File.separator + entity.getId() + File.separator + entity.getId() + ".m3u8";
                    File fromFile = new File(absolutePath);
                    if (!fromFile.exists()) {
                        log.warn("[bigVideo2m3u8] 源文件不存在, 跳过: {}", absolutePath);
                        return Mono.empty();
                    }
                    String toFilePath = r.fileBaseDir() + File.separator + "cache" + m3u8Path ;
                    File toFile = new File(toFilePath);
                    // 重切保护:先清空该视频的旧切片目录,避免失败重跑留下孤儿分片
                    if (toFile.getParentFile().exists()) {
                        FileUtil.del(toFile.getParentFile());
                    }
                    r.createParentDirectories(toFile);
                    FfmpegService.convert2m3u8(
                            absolutePath, toFilePath
                    );
                    entity.setM3u8Path(m3u8Path);
                    return PostgresqlService.getBean().template().update(entity);
                })
                .count()
                .map(count -> {
                    log.info("[bigVideo2m3u8] 完成 {} 个, 耗时 {} ms", count, (System.currentTimeMillis() - start));
                    return count;
                });

    }

    /**
     * 图片回填 pHash 感知哈希(近似重复检测用)
     * - 仅常用图片格式(OpenCV imread 支持;gif 不支持,跳过)
     * - 串行执行;解码失败的跳过并告警
     * - 注:eHash 方案已实现待启用(ImageHashService.computeHashes 可同时算双指纹,
     *        t_base_file.ehash 列已建);启用时把 WHERE 改为 (phash is null or ehash is null)
     *        并改用 computeHashes 回填两列
     */
    public Mono<Long> 图片补充phash() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and type like 'image%' and lower(suffix) in ('jpg','jpeg','png','bmp','webp') and (phash is null)")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .concatMap(entity -> {
                    String absolutePath = entity.getAbsolutePath();
                    File f = new File(absolutePath);
                    if (!f.exists()) {
                        log.warn("[图片补充phash] 源文件不存在, 跳过: {}", absolutePath);
                        return Mono.empty();
                    }
                    String hash = ImageHashService.pHash(absolutePath);
                    if (StringUtils.isBlank(hash)) {
                        log.warn("[图片补充phash] 解码失败, 跳过: {}", absolutePath);
                        return Mono.empty();
                    }
                    entity.setPhash(hash);
                    return PostgresqlService.getBean().template().update(entity);
                })
                .count()
                .map(count -> {
                    log.info("[图片补充phash] 完成 {} 个, 耗时 {} ms", count, (System.currentTimeMillis() - start));
                    return count;
                });
    }

    /**
     * 视频回填 vphash(视频封面帧指纹,相似视频初筛用)
     * - 指纹来源:thumbnail 字段的缩略图,回退 {absolutePath}.jpg
     * - 抗转码/分辨率变化(32x32 下采样);对加水印首帧有一定鲁棒性
     * - 串行执行,幂等(只补 vphash is null)
     */
    public Mono<Long> 视频补充vphash() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and type like 'video%' and (vphash is null)")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .concatMap(entity -> {
                    String thumbPath = ImageHashService.thumbnailPath(entity.getThumbnail(), entity.getAbsolutePath());
                    if (thumbPath == null) {
                        log.warn("[视频补充vphash] 缩略图不存在, 跳过: id={} thumbnail={}", entity.getId(), entity.getThumbnail());
                        return Mono.empty();
                    }
                    String hash = ImageHashService.pHash(thumbPath);
                    if (StringUtils.isBlank(hash)) {
                        log.warn("[视频补充vphash] 解码失败, 跳过: {}", thumbPath);
                        return Mono.empty();
                    }
                    entity.setVphash(hash);
                    return PostgresqlService.getBean().template().update(entity);
                })
                .count()
                .map(count -> {
                    log.info("[视频补充vphash] 完成 {} 个, 耗时 {} ms", count, (System.currentTimeMillis() - start));
                    return count;
                });
    }

    /**
     * 视频回填多帧指纹 vhash(方案 B:均匀抽 12 帧 + 中心裁剪 + 每帧 pHash)
     * - 抗水印/转码/分辨率变化;覆盖缩略图全黑导致 vphash 无效的视频
     * - 每个视频需 ffprobe 探时长 + 12 次关键帧 seek,HDD 上约 3~5 秒/个
     * - 串行执行,幂等(只补 vhash is null)
     */
    public Mono<Long> 视频补充vhash() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and type like 'video%' and (vhash is null)")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .concatMap(entity -> {
                    String absolutePath = entity.getAbsolutePath();
                    String[] hashes = VideoFrameHashService.frameHashes(absolutePath, VideoFrameHashService.DEFAULT_FRAMES);
                    if (hashes == null || hashes.length == 0) {
                        // 打"已尝试"标记(vhash 空串 + 帧数 0):避免坏文件/超时文件在后续回填中被无限重试
                        log.warn("[视频补充vhash] 抽帧失败, 标记跳过: id={}", entity.getId());
                        entity.setVhash("");
                        entity.setVhashFrames(0);
                        return PostgresqlService.getBean().template().update(entity);
                    }
                    entity.setVhash(VideoFrameHashService.join(hashes));
                    entity.setVhashFrames(hashes.length);
                    return PostgresqlService.getBean().template().update(entity);
                })
                .count()
                .map(count -> {
                    log.info("[视频补充vhash] 完成 {} 个, 耗时 {} ms", count, (System.currentTimeMillis() - start));
                    return count;
                });
    }

    /**
     * 图片相似分组(页面"相似"选项用):按 phash 汉明距离聚类,给每组打 similar_group 标签
     * - 并查集:10000 张图约 5000 万次比对,Java 侧毫秒级~秒级
     * - 组标签取锚点图的 phash(组内唯一);退化指纹(全黑等)不参与
     * - 全量重算:先清空旧标签再写入(阈值调整后重跑即可)
     */
    public Mono<Long> 图片补充相似分组() {
        long start = System.currentTimeMillis();
        final int THRESHOLD = 10;   // 汉明距离阈值,与前端 find-similar 默认一致
        return Mono.fromCallable(() -> {
            DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
            List<Map<String, Object>> rows = client.sql("""
                            select id, phash from T_BASE_FILE
                            where IS_DELETED = '0' and type like 'image%' and phash is not null
                              and bit_count(('x'||phash)::bit(64)) between 8 and 56
                            """)
                    .fetch().all().collectList().block();
            int n = rows == null ? 0 : rows.size();
            if (n == 0) return 0L;

            long[] ids = new long[n];
            long[] hashes = new long[n];
            for (int i = 0; i < n; i++) {
                Map<String, Object> row = rows.get(i);
                ids[i] = ((Number) row.get("id")).longValue();
                hashes[i] = Long.parseUnsignedLong((String) row.get("phash"), 16);
            }

            // 并查集
            int[] parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (Long.bitCount(hashes[i] ^ hashes[j]) <= THRESHOLD) {
                        int ri = find(parent, i);
                        int rj = find(parent, j);
                        if (ri != rj) parent[rj] = ri;
                    }
                }
            }

            // 归组:仅保留成员数 >= 2 的组
            Map<Integer, List<Long>> groups = new HashMap<>();
            for (int i = 0; i < n; i++) {
                groups.computeIfAbsent(find(parent, i), k -> new ArrayList<>()).add(ids[i]);
            }

            // 先清空旧标签
            client.sql("update t_base_file set similar_group = null where similar_group is not null")
                    .fetch().rowsUpdated().block();

            long groupCount = 0;
            for (Map.Entry<Integer, List<Long>> e : groups.entrySet()) {
                List<Long> memberIds = e.getValue();
                if (memberIds.size() < 2) continue;
                String label = "ph:" + String.format("%016x", hashes[e.getKey()]);
                String inList = memberIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                client.sql("update t_base_file set similar_group = :label where id in (" + inList + ")")
                        .bind("label", label)
                        .fetch().rowsUpdated().block();
                groupCount++;
            }
            return groupCount;
        })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(c -> log.info("[图片补充相似分组] 完成 {} 组, 耗时 {} ms", c, (System.currentTimeMillis() - start)));
    }

    private static int find(int[] parent, int i) {
        while (parent[i] != i) {
            parent[i] = parent[parent[i]];
            i = parent[i];
        }
        return i;
    }

    /**
     * 视频相似分组(页面"视频相似"选项用):按多帧指纹命中聚类,给每组打 video_similar_group 标签
     * - 判据:两视频对应帧汉明距离 ≤8 的帧数 ≥ max(4, ceil(较短帧数 × 0.6))
     * - 并查集:3000 个视频约 460 万对 × 12 帧 ≈ 5500 万次位比较,Java 侧约 1~3 秒
     * - 仅对有 vhash(多帧指纹)的视频生效;全量重算(先清旧标签)
     */
    public Mono<Long> 视频补充相似分组() {
        long start = System.currentTimeMillis();
        final int FRAME_THRESHOLD = 8;
        return Mono.fromCallable(() -> {
            DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
            List<Map<String, Object>> rows = client.sql("""
                            select id, vhash from T_BASE_FILE
                            where IS_DELETED = '0' and type like 'video%' and vhash is not null and vhash <> ''
                            """)
                    .fetch().all().collectList().block();
            int n = rows == null ? 0 : rows.size();
            if (n == 0) return 0L;

            long[] ids = new long[n];
            long[][] hashes = new long[n][];
            for (int i = 0; i < n; i++) {
                Map<String, Object> row = rows.get(i);
                ids[i] = ((Number) row.get("id")).longValue();
                String[] parts = ((String) row.get("vhash")).split(",");
                long[] hs = new long[parts.length];
                for (int k = 0; k < parts.length; k++) {
                    hs[k] = Long.parseUnsignedLong(parts[k].trim(), 16);
                }
                hashes[i] = hs;
            }

            int[] parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    int len = Math.min(hashes[i].length, hashes[j].length);
                    if (len < 4) continue;                    // 帧数太少不参与
                    int required = Math.max(4, (int) Math.ceil(len * 0.6));
                    int hits = 0;
                    for (int k = 0; k < len && hits < required; k++) {
                        if (Long.bitCount(hashes[i][k] ^ hashes[j][k]) <= FRAME_THRESHOLD) hits++;
                    }
                    if (hits >= required) {
                        int ri = find(parent, i);
                        int rj = find(parent, j);
                        if (ri != rj) parent[rj] = ri;
                    }
                }
            }

            Map<Integer, List<Long>> groups = new HashMap<>();
            for (int i = 0; i < n; i++) {
                groups.computeIfAbsent(find(parent, i), k -> new ArrayList<>()).add(ids[i]);
            }

            client.sql("update t_base_file set video_similar_group = null where video_similar_group is not null")
                    .fetch().rowsUpdated().block();

            long groupCount = 0;
            for (Map.Entry<Integer, List<Long>> e : groups.entrySet()) {
                List<Long> memberIds = e.getValue();
                if (memberIds.size() < 2) continue;
                String label = "vh:" + ids[e.getKey()];
                String inList = memberIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                client.sql("update t_base_file set video_similar_group = :label where id in (" + inList + ")")
                        .bind("label", label)
                        .fetch().rowsUpdated().block();
                groupCount++;
            }
            return groupCount;
        })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(c -> log.info("[视频补充相似分组] 完成 {} 组, 耗时 {} ms", c, (System.currentTimeMillis() - start)));
    }

    @NotNull
    public Mono<Long> pixiv补充pixivCode() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and code = 'pixiv' and pixiv_code is null ")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .flatMap(entity -> {
                    String fileName = entity.getFileName();
                    if (StringUtils.isBlank(fileName)) return Mono.empty();
                    // 从文件名提取 pixivCode: "144428583_p4.png" / "144428583_-4.png" / "140720083.gif" → "144428583" / "140720083"
                    String pixivCode = null;
                    // 先尝试分隔符模式: "144428583_p4.png" → "144428583"
                    String[] separators = {"_p", "_-", "_", "-"};
                    for (String sep : separators) {
                        int idx = fileName.indexOf(sep);
                        if (idx > 0) {
                            String candidate = fileName.substring(0, idx);
                            if (candidate.matches("\\d+")) {
                                pixivCode = candidate;
                                break;
                            }
                        }
                    }
                    // 没有分隔符，尝试整个文件名（不含后缀）: "140720083.gif" → "140720083"
                    if (pixivCode == null) {
                        int dot = fileName.lastIndexOf(".");
                        String nameWithoutExt = dot > 0 ? fileName.substring(0, dot) : fileName;
                        if (nameWithoutExt.matches("\\d+")) {
                            pixivCode = nameWithoutExt;
                        }
                    }
                    if (pixivCode == null) return Mono.empty();
                    entity.setPixivCode(pixivCode);
                    return PostgresqlService.getBean().template().update(entity);
                })
                .count()
                .map(count -> {
//                    log.info("[pixiv补充pixivCode] count = {}, cost = {}ms", count, (System.currentTimeMillis() - start));
                    return count;
                });
    }
}
