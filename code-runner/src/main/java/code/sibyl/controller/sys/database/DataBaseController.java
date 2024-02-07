package code.sibyl.controller.sys.database;

import code.sibyl.common.Response;
import code.sibyl.domain.Database;
import code.sibyl.dto.DatabaseDTO;
import code.sibyl.repository.DatabaseRepository;
import code.sibyl.util.r;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/system/database")
@Slf4j
public class DataBaseController {

    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("database/list-view"));
    }

    @GetMapping("/add-view")
    public Mono<String> add_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("database/add-view"));
    }

    @PostMapping("/list")
    @ResponseBody
    public Flux<Database> findAll() {
        return r.getBean(DatabaseRepository.class).list();
    }
}
