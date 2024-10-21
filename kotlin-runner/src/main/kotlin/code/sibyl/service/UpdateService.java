package code.sibyl.service;

import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

    public Mono<Long> r_18_clear() {
        Path root = Path.of(r.fileBaseDir);
        Criteria criteria = Criteria.where("IS_DELETED").is("1");
        return r2dbcEntityTemplate.select(Query.query(criteria), BaseFile.class).flatMap(e -> {
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

    public Mono<Long> r_18_init() {
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
                //.then()
                .flatMapMany(_ -> Flux.create((sink) -> {
                    try (Stream<Path> files = Files.walk(root)) {
                        files.forEach(e -> sink.next(e));
                        sink.complete();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        sink.error(exception);
                    }
                }))
                .publishOn(Schedulers.parallel())
                .filter(item -> {
                    Path path = (Path) item;
                    File realFile = path.toFile();
                    //System.err.println(path.getFileName() + " -> " + realFile.isFile() + " -> " + realFile.isDirectory());
                    return realFile.isFile() && !realFile.isDirectory();
                })
                .flatMap(item -> {
                    try {
                        Path path = (Path) item;
                        String fileName = path.getFileName().toString();
                        BaseFile file = this.file(fileName, path.toAbsolutePath().toString(), "pixiv", null, false);
                        System.err.println(file.getAbsolutePath());
                        return r2dbcEntityTemplate.insert(file);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return Mono.empty();
                    }
                })
                .count()
                .map(count -> {
                    System.err.println(STR."pixiv_init -> \{count} cost = \{(System.currentTimeMillis() - start)}");
                    return count;
                });
    }


    public BaseFile file(String fileName, @NotNull String absolutePath, String code, byte[] bytes, boolean needCreateFile) {
        absolutePath = absolutePath.replace("\\", "/");
        BaseFile baseFile = new BaseFile();
        baseFile.setFileName(fileName);

        File realFile = new File(absolutePath);
        if (Objects.nonNull(bytes) && needCreateFile) {
            String hash = r.hashBytes(bytes);
            baseFile.setType(r.getBean(Tika.class).detect(bytes));
            baseFile.setSha256(hash);
        } else {
            try {
                baseFile.setType(r.getBean(Tika.class).detect(realFile));
                baseFile.setSha256(r.computeFileSHA256(realFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.err.println(absolutePath);
        baseFile.setAbsolutePath(absolutePath);
        //System.err.println(absolutePath.replace(r.fileBaseDir, ""));
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
            realFile = new File(absolutePath);
            BufferedImage image = ImageIO.read(realFile);
            if (baseFile.getType().contains("image") && r.isImage(realFile) && Objects.nonNull(image)) {
                int width = image.getWidth();
                int height = image.getHeight();
                baseFile.setWidth(width);
                baseFile.setHeight(height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseFile.setSize(Objects.nonNull(bytes) ? bytes.length : realFile.length());

        return baseFile;
    }

    @NotNull
    public Mono<Long> 文件补充hash() {
        return Mono.empty();
    }
}
