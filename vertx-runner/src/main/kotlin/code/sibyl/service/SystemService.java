package code.sibyl.service;

import code.sibyl.common.Bean;
import code.sibyl.common.r;
import code.sibyl.database.Repository;
import code.sibyl.domain.BaseFile;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.parallel.ParallelFlowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.sqlclient.templates.RowMapper;
import io.vertx.rxjava3.sqlclient.templates.SqlTemplate;
import org.apache.tika.Tika;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;

public class SystemService implements Bean {

    public void main(String[] args) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        Flowable.create(sink -> {
                    try (Stream<Path> files = Files.walk(Path.of(r.fileBaseDir))) {
                        files.forEach(e -> sink.onNext(e));
                        sink.onComplete();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        sink.onNext(exception);
                    }
                }, BackpressureStrategy.BUFFER)
                .parallel()
                .runOn(Schedulers.computation())

//        Flowable.range(1, 10)
//                .parallel(64)
//                .runOn(Schedulers.computation());

                .flatMap(e -> {
                    System.err.println(STR."\{Thread.currentThread()} -> \{e}");
                    Thread.sleep(1000);
                    return Flowable.just(e);
                })
//                .doOnError(throwable -> throwable.printStackTrace())
                .sequential()
                .count()
//                .sequential()
//                .reduce((a, b) -> a + b)
//                .count()
                .doOnSuccess(count -> System.err.println(STR."doOnSuccess end -> \{Thread.currentThread()}, count = \{count}, cost = \{System.currentTimeMillis() - start}"))
//                .doOnComplete(() -> System.err.println(STR."doOnComplete end -> \{Thread.currentThread()} cost -> \{System.currentTimeMillis() - start}"))
//                .doFinally(() -> System.err.println(STR."doFinally end -> \{Thread.currentThread()} cost -> \{System.currentTimeMillis() - start}"))
                .subscribe();

        ;

        Thread.sleep(10000000);
    }


    public SystemService() {
        init();
    }

    private static SystemService instance = new SystemService();

    public static SystemService getInstance() {
        return instance;
    }

    public void pixiv_init(Vertx vertx) {
        long start = System.currentTimeMillis();
        System.err.println(STR."start -> \{start}");
        Tika tika = new Tika();

        vertx.fileSystem()
                .rxReadDir(r.fileBaseDir)
                .flattenAsFlowable(list -> list)
                .filter(e -> new File(e).isFile())
                //.compose(e -> e.compose(e -> e.compose()))
                .flatMap(absolutePath -> {
                    File realFile = new File(absolutePath);
                    absolutePath = absolutePath.replace("\\", "/");
                    BaseFile baseFile = new BaseFile();
                    String fileName = realFile.getName();
                    baseFile.setFileName(fileName);

                    baseFile.setType(tika.detect(realFile));
                    baseFile.setSha256(r.computeFileSHA256(realFile));

                    baseFile.setAbsolutePath(absolutePath);
                    baseFile.setRelativePath(absolutePath.replace(r.fileBaseDir, ""));

                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    baseFile.setSuffix(suffix);
                    baseFile.setCode("pixiv");
                    baseFile.setSerialNumber(null);
                    baseFile.setDeleted("0");
                    baseFile.setCreateId(0L);
                    baseFile.setCreateTime(LocalDateTime.now());

                    try {
                        if (baseFile.getType().contains("image")) {
                            try {
                                BufferedImage image = ImageIO.read(realFile);
                                int width = image.getWidth();
                                int height = image.getHeight();
                                baseFile.setWidth(width);
                                baseFile.setHeight(height);
                            } catch (Exception e) {
                                System.err.println(STR."图片读取异常 ->  \{baseFile.getAbsolutePath()}");
                                e.printStackTrace();
                                return Flowable.error(e);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    baseFile.setSize(realFile.length());
                    System.err.println(STR."\{Thread.currentThread()} -> \{baseFile.getAbsolutePath()}");

                    return Flowable.fromSingle(
                            SqlTemplate
                                    .forUpdate(Repository.getInstance().getJdbcPool(), """
                                            insert into T_BASE_FILE(FILE_NAME, TYPE, ABSOLUTE_PATH, SIZE, SUFFIX, SERIAL_NUMBER, SHA256, CODE, WIDTH, HEIGHT, IS_DELETED, CREATE_ID, CREATE_TIME,  RELATIVE_PATH)
                                            values (#{fileName}, #{type}, #{absolutePath}, #{size}, #{suffix}, #{serialNumber}, #{sha256}, #{code}, #{width}, #{height}, 0, 0, now(), #{relativePath});
                                            """)
                                    .mapFrom(BaseFile.class)
                                    .rxExecute(baseFile)
                                    .doOnError(throwable -> throwable.printStackTrace())
                    )
                            //.subscribe()
                            ;
                })
                .count()
                .doOnSuccess(count -> System.err.println(STR."end -> \{Thread.currentThread()} -> \{count} , cost = \{System.currentTimeMillis() - start}"))
                .subscribe()
        ;
    }

    public void pixiv_init_parallel(Vertx vertx) {
        long start = System.currentTimeMillis();
        System.err.println(STR."start -> \{start}");
        Tika tika = new Tika();

        vertx.fileSystem()
                .rxReadDir(r.fileBaseDir)
                .flattenAsFlowable(list -> list)
                .parallel()
                .runOn(Schedulers.io())
                .filter(e -> new File(e).isFile())
                .flatMap(absolutePath -> {
                    File realFile = new File(absolutePath);
                    absolutePath = absolutePath.replace("\\", "/");
                    BaseFile baseFile = new BaseFile();
                    String fileName = realFile.getName();
                    baseFile.setFileName(fileName);

                    baseFile.setType(tika.detect(realFile));
                    baseFile.setSha256(r.computeFileSHA256(realFile));

                    //System.err.println(absolutePath);
                    baseFile.setAbsolutePath(absolutePath);
                    //System.err.println(absolutePath.replace(r.fileBaseDir, ""));
                    baseFile.setRelativePath(absolutePath.replace(r.fileBaseDir, ""));

                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    baseFile.setSuffix(suffix);
                    baseFile.setCode("pixiv");
                    baseFile.setSerialNumber(null);
                    baseFile.setDeleted("0");
                    baseFile.setCreateId(0L);
                    baseFile.setCreateTime(LocalDateTime.now());

                    try {
                        if (baseFile.getType().contains("image")) {
                            try {
                                BufferedImage image = ImageIO.read(realFile);
                                int width = image.getWidth();
                                int height = image.getHeight();
                                baseFile.setWidth(width);
                                baseFile.setHeight(height);
                            } catch (Exception e) {
                                System.err.println(STR."图片读取异常 ->  \{baseFile.getAbsolutePath()}");
                                e.printStackTrace();
                                throw e;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    baseFile.setSize(realFile.length());
                    System.err.println(STR."\{Thread.currentThread()} -> \{baseFile}");

                    return SqlTemplate
                            .forUpdate(Repository.getInstance().getJdbcPool(), """
                                    insert into T_BASE_FILE(FILE_NAME, TYPE, ABSOLUTE_PATH, SIZE, SUFFIX, SERIAL_NUMBER, SHA256, CODE, WIDTH, HEIGHT, IS_DELETED, CREATE_ID, CREATE_TIME,  RELATIVE_PATH)
                                    values (#{fileName}, #{type}, #{absolutePath}, #{size}, #{suffix}, #{serialNumber}, #{sha256}, #{code}, #{width}, #{height}, 0, 0, now(), #{relativePath});
                                    """)
                            .mapFrom(BaseFile.class)
                            .rxExecute(baseFile)
                            .toFlowable();


//                    return Flowable.fromSingle(
//                            SqlTemplate
//                                    .forUpdate(Repository.getInstance().getJdbcPool(), """
//                                            insert into T_BASE_FILE(FILE_NAME, TYPE, ABSOLUTE_PATH, SIZE, SUFFIX, SERIAL_NUMBER, SHA256, CODE, WIDTH, HEIGHT, IS_DELETED, CREATE_ID, CREATE_TIME,  RELATIVE_PATH)
//                                            values (#{fileName}, #{type}, #{absolutePath}, #{size}, #{suffix}, #{serialNumber}, #{sha256}, #{code}, #{width}, #{height}, 0, 0, now(), #{relativePath});
//                                            """)
//                                    .mapFrom(BaseFile.class)
//                                    .rxExecute(baseFile)
//                                    .doOnError(throwable -> throwable.printStackTrace())
//
//                    )
                    //.subscribe()
//                            ;
                })
                .sequential()
                .count()
                .doOnSuccess(count -> System.err.println(STR."end -> \{Thread.currentThread()} -> \{count} , cost = \{(System.currentTimeMillis() - start) / 1000 / 60}"))
                .subscribe()
        ;
    }
}


