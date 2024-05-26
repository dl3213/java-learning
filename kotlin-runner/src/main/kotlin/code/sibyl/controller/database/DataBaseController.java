package code.sibyl.controller.database;

import code.sibyl.aop.Header;
import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.domain.database.Database;
import code.sibyl.service.DataBaseService;
import code.sibyl.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/database")
@Slf4j
@RequiredArgsConstructor
public class DataBaseController {

    private final DataBaseService dataBaseService;
    private final TestService testService;
//    private final CommonRepository commonRepository;


    @SneakyThrows
    @GetMapping("/list-view")
    public Mono<String> list_view(final Model model) {

        return Mono.create(monoSink -> {
            List<String> headerList = Arrays.stream(Database.class.getDeclaredFields())
                    .filter(e -> Objects.nonNull(e.getAnnotation(Header.class)))
                    .map(Field::getName)
                    .collect(Collectors.toList());
            model.addAttribute("headerList", headerList);
            model.addAttribute("systemName", r.systemName());
            model.addAttribute("title", r.systemName());
            monoSink.success("database/list-view");
        });

//        return dataBaseService.list()
//                .collectList()
//                .doOnSuccess(list -> {
//                    model.addAttribute("list", list);
//                    List<String> headerList = Arrays.stream(Database.class.getDeclaredFields())
//                            .filter(e -> Objects.nonNull(e.getAnnotation(Header.class)))
//                            .map(Field::getName)
//                            .collect(Collectors.toList());
//                    model.addAttribute("headerList", headerList);
//                    model.addAttribute("systemName", r.systemName());
//                    model.addAttribute("title", r.systemName());
//                })
//                .flatMap(e -> Mono.create(monoSink -> monoSink.success("database/list-view")));
//        return Mono.create(monoSink -> {
//            List<String> headerList = Arrays.stream(Database.class.getDeclaredFields())
//                    .filter(e -> Objects.nonNull(e.getAnnotation(Header.class)))
//                    .map(Field::getName)
//                    .collect(Collectors.toList());
//            model.addAttribute("headerList", headerList);
//            model.addAttribute("systemName", r.systemName());
//            model.addAttribute("title", r.systemName());
//            monoSink.success("database/list-view");
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
        return r.successMono();
    }

    @PostMapping("/backup/{id}")
    @ResponseBody
    public Mono<Response> backup(@PathVariable String id) {
        dataBaseService.backup(id);
        return r.successMono();
    }
}
