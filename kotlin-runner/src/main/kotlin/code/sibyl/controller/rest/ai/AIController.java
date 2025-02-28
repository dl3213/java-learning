package code.sibyl.controller.rest.ai;

import code.sibyl.common.DataBaseTypeEnum;
import code.sibyl.common.Response;
import code.sibyl.common.r;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Controller
@RequestMapping("/api/rest/v1/ai")
@RequiredArgsConstructor
public class AIController {

    @GetMapping("/view.html")
    public Mono<String> add_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("ai/list-view"));
    }
    @PostMapping("/list")
    @ResponseBody
    public Mono<Response> list(@RequestBody JSONObject jsonObject) {
        JSONObject json = new JSONObject();
        json.put("code","deepseek");
        json.put("name","deepseek");
        json.put("desc","deepseek");
        return Mono.just(Arrays.asList(json)).map(e -> r.success(e));
    }
}
