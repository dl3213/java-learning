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
        return PostgresqlService.getBean().template().getDatabaseClient().sql("select * from t_biz_book_20250325 where is_deleted = '1'")
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
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and SHA256 IS NULL ")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .take(500)
                .flatMap(item -> BaseFileHandler.getBean().sha256(item))
                .count()
                .map(count -> {
                    log.info("[文件补充hash] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    @NotNull
    public Mono<Long> 图片补充大小() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where type like 'image%' and (WIDTH is null or HEIGHT is null)")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .take(100)
                .flatMap(item -> BaseFileHandler.getBean().图片补充大小(item))
                .count()
                .map(count -> {
                    log.info("[图片补充大小] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    @NotNull
    public Mono<Long> 视频文件补充thumbnail() {
        long start = System.currentTimeMillis();
        DatabaseClient client = PostgresqlService.getBean().template().getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where IS_DELETED = '0' and type like 'video%' and (thumbnail is null)")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .take(100)
                .flatMap(item -> BaseFileHandler.getBean().视频文件补充thumbnail(item))
                .count()
                .map(count -> {
                    log.info("[视频文件补充thumbnail] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }


}
