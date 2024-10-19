package code.sibyl.service;

import code.sibyl.common.r;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    private final Path root = Paths.get(r.INSTANCE.getBaseDir());

    public void init() {
//        try {
//            Files.createDirectories(root);
//            r.INSTANCE.setBaseDir("");
//        } catch (IOException e) {
//            throw new RuntimeException("Could not initialize folder for upload!");
//        }
    }

    public Mono<String> save(Mono<FilePart> filePartMono) {

        return filePartMono.doOnNext(fp -> System.out.println("Receiving File:" + fp.filename())).flatMap(filePart -> {
            String filename = filePart.filename();
            return filePart.transferTo(root.resolve(filename)).then(Mono.just(filename));
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