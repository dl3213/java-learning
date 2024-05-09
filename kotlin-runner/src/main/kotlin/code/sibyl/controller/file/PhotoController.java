package code.sibyl.controller.file;

import code.sibyl.common.r;
import code.sibyl.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    private final static String dir = r.baseDir() + File.separator + "photo";

    @Autowired
    FileService fileService;

    @GetMapping({"/list-view"})
    public Mono<String> model(final Model model) {
        String s = "/photo/list-view";
        model.addAttribute("systemName", r.systemName());
        model.addAttribute("title", r.systemName());
        return Mono.create(monoSink -> monoSink.success(s));
    }

    @GetMapping("/load/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Flux<DataBuffer>> load(@PathVariable String filename) {
        Flux<DataBuffer> file = fileService.load(Path.of(dir), filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
    }

    @GetMapping("/load/list")
    @ResponseBody
    public ResponseEntity<Flux<DataBuffer>> loadList() {
        Stream<Path> pathStream = fileService.loadAll(Path.of(dir));
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
        return null;
    }
}
