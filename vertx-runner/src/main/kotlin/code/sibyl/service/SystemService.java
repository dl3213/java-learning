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

}


