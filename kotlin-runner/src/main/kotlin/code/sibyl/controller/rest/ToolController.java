package code.sibyl.controller.rest;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import code.sibyl.service.ToolService;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.InputStream;

@Controller
@RequestMapping("/api/rest/v1/tool")
@Slf4j
public class ToolController {

    @GetMapping("/view.html")
    public Mono<String> add_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("tool/view"));
    }

    @PostMapping("/bilibili/url")
    @ResponseBody
    public Mono<Response> get_bv_url(@RequestBody JSONObject jsonObject) {

        return Mono.just(jsonObject)
                .map(json -> {
                    String cookie = json.getString("cookie");
                    String bv = json.getString("bv");
                    return r.success(ToolService.getBean().bv_url(bv, cookie));
                })
                ;
    }

    @PostMapping("/bilibili/download")
    @ResponseBody
    public Mono<ResponseEntity<Flux<DataBuffer>>> downloadFile(@RequestBody JSONObject jsonObject) {
        try {
            String cookie = jsonObject.getString("cookie");
            String bv = jsonObject.getString("bv");

            InputStream inputStream = ToolService.getBean().bv_download(bv, cookie);
            DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

            // 将InputStream转换为Flux<DataBuffer>，并确保资源释放
            Flux<DataBuffer> flux = DataBufferUtils.readInputStream(
                    () -> inputStream,
                    bufferFactory,
                    4096 // 缓冲区大小
            ).publishOn(Schedulers.boundedElastic()); // 使用弹性线程池避免阻塞

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", bv + ".mp4");

            return Mono.just(
                    ResponseEntity.ok()
                            .headers(headers)
                            .body(flux)
            );
        } catch (Exception e) {
            return Mono.just(ResponseEntity.notFound().build());
        }
    }

}
