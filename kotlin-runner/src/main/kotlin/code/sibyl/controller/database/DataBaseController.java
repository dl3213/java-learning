package code.sibyl.controller.database;

import code.sibyl.common.DataBaseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/database")
@Slf4j
@RequiredArgsConstructor
public class DataBaseController {

    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("database/list-view"));
    }

    @GetMapping("/add-view")
    public Mono<String> add_view(final Model model) {
        model.addAttribute("typeList", DataBaseTypeEnum.values());
        return Mono.create(monoSink -> monoSink.success("database/add-view"));
    }
}
