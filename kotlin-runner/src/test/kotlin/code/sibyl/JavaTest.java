package code.sibyl;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JavaTest {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Test
    public void testChatModel() {

        System.err.println(chatModel);
        System.err.println(ollamaChatModel);

//        ChatClient chatClient = ChatClient.builder(chatModel)
//                // 实现 Chat Memory 的 Advisor
//                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
//                .defaultAdvisors(
//                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
//                )
//                // 实现 Logger 的 Advisor
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor()
//                )
//                // 设置 ChatClient 中 ChatModel 的 Options 参数
//                .defaultOptions(
//                        OllamaOptions.builder()
//                                .topP(0.7)
//                                .build()
//                )
//                .build();
//
//        String prompt = """
//                你是一个精通中文和英文的翻译大师。如果我给你英文就翻译成中文，给你中文就翻译成英文。
//                """;
//        String message = """
//                Difficult times show us who our true friends are.
//                """;
//
//        String result = ollamaChatModel.call(prompt + ":" + message);
//
//        System.out.println(result);
    }
}
