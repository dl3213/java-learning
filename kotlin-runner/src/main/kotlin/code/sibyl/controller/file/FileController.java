package code.sibyl.controller.file;

import code.sibyl.aop.Header;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import code.sibyl.model.FileInfo;
import code.sibyl.service.DataBaseService;
import code.sibyl.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService storageService;
    @Autowired
    private DataBaseService dataBaseService;

    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {
        return dataBaseService.list()
                .collectList()
                .doOnSuccess(list -> {
                    model.addAttribute("list", list);
                    List<String> headerList = Arrays.stream(Database.class.getDeclaredFields())
                            .filter(e -> Objects.nonNull(e.getAnnotation(Header.class)))
                            .map(Field::getName)
                            .collect(Collectors.toList());
                    model.addAttribute("headerList", headerList);
                    model.addAttribute("systemName", r.systemName());
                    model.addAttribute("title", r.systemName());
                })
                .flatMap(e -> Mono.create(monoSink -> monoSink.success("file/list-view")));
    }

    @PostMapping("/upload")
    @ResponseBody
    public Mono<ResponseEntity<Response>> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
        return storageService.save(filePartMono).map(
                (filename) -> ResponseEntity.ok().body(Response.success("Uploaded the file successfully: " + filename)));
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Flux<FileInfo>> getListFiles() {
        Stream<FileInfo> fileInfoStream = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = UriComponentsBuilder.newInstance().path("/files/{filename}").buildAndExpand(filename).toUriString();
            File file = new File(r.baseDir() + File.separator + path.getFileName());
            return new FileInfo(filename, url, 0l, file.isDirectory(), file.isFile());
        });

        Flux<FileInfo> fileInfosFlux = Flux.fromStream(fileInfoStream);

        return ResponseEntity.status(HttpStatus.OK).body(fileInfosFlux);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Flux<DataBuffer>> getFile(@PathVariable String filename) {
        Flux<DataBuffer> file = storageService.load(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
    }

    @DeleteMapping("/files/{filename:.+}")
    @ResponseBody
    public Mono<ResponseEntity<Response>> deleteFile(@PathVariable String filename) {
        String message = "";

        try {
            boolean existed = storageService.delete(filename);

            if (existed) {
                message = "Delete the file successfully: " + filename;
                return Mono.just(ResponseEntity.ok().body(Response.success(message)));
            }

            message = "The file does not exist!";
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.success(message)));
        } catch (Exception e) {
            message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.success(message)));
        }
    }


}
