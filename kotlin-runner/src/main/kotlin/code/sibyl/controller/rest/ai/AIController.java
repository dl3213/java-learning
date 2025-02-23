package code.sibyl.controller.rest.ai;

import code.sibyl.common.DataBaseTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/rest/v1/ai")
@RequiredArgsConstructor
public class AIController {

    @GetMapping("/view.html")
    public Mono<String> add_view(final Model model) {
        return Mono.create(monoSink -> monoSink.success("ai/list-view"));
    }
}
