package code.sibyl.service;

import cn.hutool.core.lang.Snowflake;
import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@Deprecated
public class FileService {

    private final Path root = Paths.get(r.fileBaseDir());


    @Autowired
//    @Qualifier("sibyl-postgresql")
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    public void init() {
//        try {
//            Files.createDirectories(root);
//            r.INSTANCE.setBaseDir("");
//        } catch (IOException e) {
//            throw new RuntimeException("Could not initialize folder for upload!");
//        }
    }

    public Mono<BaseFile> save(Mono<FilePart> filePartMono) {

        return filePartMono
                .publishOn(Schedulers.fromExecutor(r.getBean(ThreadPoolTaskExecutor.class)))
                .flatMap(filePart -> {
                    log.info("[fileUpload] {} ", STR."Receiving File:\{filePart.filename()}");
                    String filename = filePart.filename();
                    String[] split = filename.split("\\.");
                    String fileUniqueId = String.valueOf(r.getBean(Snowflake.class).nextId());
                    String tempFileName = fileUniqueId + (split != null && split.length > 0 ? "." + split[split.length - 1] : "");
                    log.info("[fileUpload] tempFileName = {}", tempFileName);
                    String absoluteDir = r.fileBaseDir() + r.yyyy_MM_dd() + File.separator;
                    log.info("[fileUpload] absoluteDir = {}", absoluteDir);
                    String absolutePath = absoluteDir + tempFileName;
                    log.info("[fileUpload] absolutePath = {}", absolutePath);
                    try {
                        FileUtils.createParentDirectories(new File(absolutePath));
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                    return filePart.transferTo(root.resolve(absolutePath)).then(Mono.zip(Mono.just(filename), Mono.just(absolutePath)));
                })
                .flatMap(item -> {
                    BaseFile file = UpdateService.getBean().file(item.getT1(), item.getT2(), null, null, false);
                    return r2dbcEntityTemplate.insert(file);
                });
    }

    public Flux<DataBuffer> load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Flux<DataBuffer> load(Path root, String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
            } else {
                //throw new RuntimeException("Could not read the file!");
                return null;
            }
        } catch (MalformedURLException e) {
            //throw new RuntimeException("Error: " + e.getMessage());
            return null;
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public Stream<Path> loadAll(Path root) {
        try {
            return Files.walk(root, 1)
                    .filter(path -> !path.equals(root))
                    .map(root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public Stream<Path> loadDir() {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root) && Files.isDirectory(path))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public Stream<Path> loadDir(Path root) {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root) && Files.isDirectory(path))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public boolean delete(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}