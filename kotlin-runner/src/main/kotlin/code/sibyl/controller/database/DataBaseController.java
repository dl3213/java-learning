package code.sibyl.controller.database;

import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.common.Response;
import code.sibyl.domain.database.Database;
import code.sibyl.service.DataBaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

        return dataBaseService.list()
                .collectList()
                .map(e -> {
//                    Flux.deferContextual(ctx -> {
//                        //ctx.stream().forEach(System.err::println);
//                        return Mono.just(ctx.get(""));
//                    }).subscribe(e -> {
//                        e
//                    });
                    return e;
                })
                .doOnSuccess(list -> {
                    model.addAttribute("list", list);
                    List<String> headerList = Arrays.stream(Database.class.getDeclaredFields()).map(Field::getName).filter(e -> !e.contains("create")).collect(Collectors.toList());
                    model.addAttribute("headerList", headerList);
                    model.addAttribute("systemName", systemName);
                    model.addAttribute("title", systemName);
                })
                .flatMap(e -> Mono.create(monoSink -> monoSink.success("database/list-view")));

//        return Mono.create(monoSink -> {
//            try {
//                List<Database> list = dataBaseService.list().collectList().toFuture().get();
//                model.addAttribute("list", list);
//                List<String> headerList = Arrays.stream(Database.class.getDeclaredFields()).map(Field::getName).filter(e -> !e.contains("create")).collect(Collectors.toList());
//                model.addAttribute("headerList", headerList);
//                model.addAttribute("systemName", systemName);
//                model.addAttribute("title", systemName);
//                monoSink.success("database/list-view");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
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

    @PreAuthorize("hasAnyAuthority('admin:api')")
    @GetMapping("/connect-view")
    public Mono<String> connect_view(final Model model, String id) throws ExecutionException, InterruptedException {

        return Mono.just(id)
                .map(e -> Long.valueOf(id))
                .flatMap(e -> dataBaseService.findById(e))
                //.map(e -> e)
                .doOnSuccess(e -> model.addAttribute("target", e))
                .flatMap(e -> Mono.create(monoSink -> monoSink.success("database/connect-view")));
//        model.addAttribute("typeList", DataBaseTypeEnum.values());
//        Database target = dataBaseService.findById(Long.valueOf(id)).toFuture().get();
//        model.addAttribute("target", target);
        //return Mono.create(monoSink -> monoSink.success("database/connect-view"));
    }

    @PostMapping("/connect/{id}")
    @ResponseBody
    public Mono<Response> connect(@PathVariable String id) {
        dataBaseService.connect(id);
        return Response.successMono();
    }
}
