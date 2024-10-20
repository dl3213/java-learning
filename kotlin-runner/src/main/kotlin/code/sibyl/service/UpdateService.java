package code.sibyl.service;

import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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

    public Mono<Long> pixiv_clear() {
        Path root = Path.of(r.pixivBaseDir);
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
                })
                .count()
                .map(e -> {
                    System.err.println(STR."pixiv_clear count = \{e}");
                    return e;
                });
    }

    public Mono<Long> pixiv_init() {
        Path root = Path.of(r.pixivBaseDir);
        return Flux.create((sink) -> {
                    try (Stream<Path> files = Files.list(root)) {
                        files.forEach(e -> sink.next(e));
                        sink.complete();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        sink.error(exception);
                    }
                })
                .flatMap(item -> {
                    try {
                        Path path = (Path) item;
                        File realFile = path.toFile();
                        byte[] bytes = r.file2byte(realFile);
                        String hash = r.hashBytes(bytes);

                        String fileName = path.getFileName().toString();
                        BaseFile file = new BaseFile();
                        file.setFileName(fileName);

                        file.setType(r.getBean(Tika.class).detect(bytes));
                        file.setAbsolutePath(path.toAbsolutePath().toString());
                        file.setSha256(hash);
                        file.setSize(realFile.length());
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        //file.setExtension(suffix);
                        file.setSuffix(suffix);
                        file.setCode("pixiv");
                        file.setDeleted("0");
                        file.setCreateId(1L);
                        file.setCreateTime(LocalDateTime.now());

                        BufferedImage image = ImageIO.read(realFile);
                        if (file.getType().contains("image") && r.isImage(realFile) && Objects.nonNull(image)) {
                            int width = image.getWidth();
                            int height = image.getHeight();
                            file.setWidth(width);
                            file.setHeight(height);
                        }
                        //System.err.println(file);
                        return r2dbcEntityTemplate.insert(file);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return Mono.empty();
                    }
                })
                .count()
                .map(e -> {
                    System.err.println(STR."pixiv_init -> \{e}");
                    return e;
                });
    }
}
