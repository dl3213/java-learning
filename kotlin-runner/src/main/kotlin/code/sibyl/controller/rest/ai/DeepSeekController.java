package code.sibyl.controller.rest.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/rest/v1/ai/deepseek")
@RequiredArgsConstructor
public class DeepSeekController {

    private final ChatClient chatClient;

    // 同步调用（非流式）
    @GetMapping("/chat")
    @Deprecated
    public String chat(@RequestParam String input) {
        return chatClient.prompt().user(input).call().content();
    }

    // 流式调用（WebFlux）
    @GetMapping("/stream")
    public Flux<String> streamChat(@RequestParam String input) {
        return chatClient.prompt().user(input).stream().content();
    }
}
