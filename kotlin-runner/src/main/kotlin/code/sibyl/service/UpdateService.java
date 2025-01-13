package code.sibyl.service;

import cn.hutool.crypto.SecureUtil;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
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
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
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
public class UpdateService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;


    public static UpdateService getBean() {
        return r.getBean(UpdateService.class);
    }

    public Mono<Long> file_clear() {
        //Path root = Path.of(r.fileBaseDir);
        Criteria criteria = Criteria.where("IS_DELETED").is("1");
        return r2dbcEntityTemplate.select(Query.query(criteria), BaseFile.class)
                .flatMap(e -> {
                    System.err.println(e.getFileName());
                    File file = new File(e.getAbsolutePath());
                    try {
                        file.delete();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return r2dbcEntityTemplate.delete(e);
                }).count().map(e -> {
                    System.err.println(STR."pixiv_clear count = \{e}");
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
        Path root = Path.of(r.fileBaseDir);
        return r2dbcEntityTemplate.getDatabaseClient()
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
                        return r2dbcEntityTemplate.insert(file);
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
        Path root = Path.of(r.fileBaseDir);
        Scheduler scheduler = Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class));
        return r2dbcEntityTemplate.getDatabaseClient()
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
                        return r2dbcEntityTemplate.insert(file).map(_ -> 1L).onErrorReturn(0L);
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
        baseFile.setRelativePath(absolutePath.replace(r.fileBaseDir, ""));

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
        DatabaseClient client = r2dbcEntityTemplate.getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where SHA256 IS NULL ")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .take(100)
                .flatMap(item -> {
                    String absolutePath = item.getAbsolutePath();
                    File realFile = new File(absolutePath);
                    String sha256 = SecureUtil.sha256(realFile);
                    log.info("[文件补充hash] [{}] {} --> sha256 = {}", Thread.currentThread().getName(), absolutePath, sha256);
                    return client.sql("update T_BASE_FILE set SHA256=:sha256 where id=:id")
                            .bind("id", item.getId())
                            .bind("sha256", sha256)
                            .fetch()
                            .rowsUpdated()
                            .onErrorResume(throwable -> {
                                throwable.printStackTrace();
                                return Mono.just(0L);
                            })
                            ;
                })
                .count()
                .map(count -> {
                    //log.info("[文件补充hash] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    @NotNull
    public Mono<Long> 图片补充大小() {
        long start = System.currentTimeMillis();
        DatabaseClient client = r2dbcEntityTemplate.getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where type like 'image%' and (WIDTH is null or HEIGHT is null)")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .take(500)
                .flatMap(item -> {
                    String absolutePath = item.getAbsolutePath();
                    File realFile = new File(absolutePath);
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(realFile);
                    } catch (IOException e) {
                        System.err.println(STR."读取图片文件异常：\{absolutePath}");
                        e.printStackTrace();
                        //throw new RuntimeException(e);
                        return Mono.error(e);
                    }
                    if (Objects.isNull(image)) {
                        return Mono.empty();
                    }
                    int width = image.getWidth();
                    int height = image.getHeight();
                    log.info("[图片补充大小] [{}] {} --> width = {}, height = {}", Thread.currentThread().getName(), absolutePath, width, height);
                    return client.sql("update T_BASE_FILE set height=:height, width=:width where id=:id")
                            .bind("id", item.getId())
                            .bind("height", height)
                            .bind("width", width)
                            .fetch()
                            .rowsUpdated();
                })
                .count()
                .map(count -> {
                    //log.info("[图片补充大小] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    @NotNull
    public Mono<Long> 视频文件补充thumbnail() {
        long start = System.currentTimeMillis();
        DatabaseClient client = r2dbcEntityTemplate.getDatabaseClient();
        return client.sql("select * from T_BASE_FILE where type like 'video%' and (thumbnail is null)")
                .mapProperties(BaseFile.class)
                .all()
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
//                .subscribeOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .take(500)
                .flatMap(item -> {
                    String absolutePath = item.getAbsolutePath();
                    System.err.println(absolutePath);
                    File targetVideo = new File(absolutePath);
                    String thumbnailPath = null;
                    try {
                        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(absolutePath);
                        ff.start();
                        String rotate = ff.getVideoMetadata("rotate");
                        Frame f;
                        int i = 0;
                        while (i < 1) {
                            f = ff.grabImage();
                            IplImage src;
                            if (null != rotate && rotate.length() > 1) {
                                OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                                src = converter.convert(f);
                                f = converter.convert(rotate(src, Integer.parseInt(rotate)));
                            }
                            thumbnailPath = doExecuteFrame(f, targetVideo.getParentFile().getAbsolutePath(), item.getFileName());
                            System.err.println(thumbnailPath);
                            i++;
                        }
                        ff.stop();

                    } catch (FFmpegFrameGrabber.Exception e) {
                        return Flux.error(new RuntimeException(e));
                    }
                    System.err.println();

                    log.info("[图片补充大小] [{}] {} --> thumbnailPath = {} " , Thread.currentThread().getName(), absolutePath , thumbnailPath );
                    return client.sql("update T_BASE_FILE set thumbnail=:thumbnailPath where id=:id")
                            .bind("id", item.getId())
                            .bind("thumbnailPath", thumbnailPath)
                            .fetch()
                            .rowsUpdated();
//                    return Mono.just(1);
                })
                .count()
                .map(count -> {
                    //log.info("[视频文件补充thumbnail] count = {}, cost = {}", count, (System.currentTimeMillis() - start));
                    return count;
                })
                ;
    }

    /**
     * 进行旋转角度操作（为了保证截取到的第一帧图片与视频中的角度方向保持一致）
     */
    public IplImage rotate(IplImage src, int angle) {
        IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core opencv_core = new opencv_core();
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, angle);
        return img;
    }

    public static String doExecuteFrame(Frame f, String targetFilePath, String targetFileName) {
        if (null == f || null == f.image) {
            return null;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "jpg";
        String fileName = targetFilePath + File.separator + targetFileName + "." + imageMat;
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(fileName);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
