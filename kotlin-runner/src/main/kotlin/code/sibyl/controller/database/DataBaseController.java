package code.sibyl.controller.database;

import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.common.Response;
import code.sibyl.domain.database.Database;
import code.sibyl.service.DataBaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/database")
@Slf4j
@RequiredArgsConstructor
public class DataBaseController {

    private final DataBaseService dataBaseService;

    private final static String systemName = "未命名";

    @SneakyThrows
    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {
        List<Database> list = dataBaseService.list().collectList().toFuture().get();
        model.addAttribute("list", list);
        List<String> headerList = Arrays.stream(Database.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        model.addAttribute("headerList", headerList);
        model.addAttribute("systemName", systemName);
        model.addAttribute("title", systemName);
        return Mono.create(monoSink -> monoSink.success("database/list-view"));
    }

    @GetMapping("/add-view")
    public Mono<String> add_view(final Model model) {
        model.addAttribute("typeList", DataBaseTypeEnum.values());
        return Mono.create(monoSink -> monoSink.success("database/add-view"));
    }

    @GetMapping("/update-view")
    public Mono<String> update_view(final Model model, String id) throws ExecutionException, InterruptedException {
        model.addAttribute("typeList", DataBaseTypeEnum.values());
        Database target = dataBaseService.findById(Long.valueOf(id)).toFuture().get();
        model.addAttribute("target", target);
        return Mono.create(monoSink -> monoSink.success("database/update-view"));
    }

    @PostMapping("/connect/{id}")
    @ResponseBody
    public Mono<Response> connect(@PathVariable String id) {
        dataBaseService.connect(id);
        return Response.successMono();
    }
}
