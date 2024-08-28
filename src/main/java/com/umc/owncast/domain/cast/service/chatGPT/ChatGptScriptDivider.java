package com.umc.owncast.domain.cast.service.chatGPT;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptScriptDivider {
    // TODO: ChatGPT 관련 서비스 하나로 합치기 (ChatGPTPromptGenerator, AnswerGenerator, TranslationService.. 다 하나로 통합)
    @Value("${open-ai.secret-key}")
    private String SECRET_KEY;
    private OpenAiService openAiService;

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();


    /** 문장 사이에 @를 삽입하도록 지시  */
    public String placeDelimiter(String script, String delimiter) {
        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a sentence parser."),
                new ChatMessage(SYSTEM, "You should divide each sentence by placing %s.".formatted(delimiter))
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Hello everyone, This is a test!"),
                new ChatMessage(ASSISTANT, "Hello everyone, This is a test!@"),
                new ChatMessage(USER, "In conclusion, the LCK continues to be a thrilling league filled with talent, competition, and passionate fans. The current season has been nothing short of exciting, with teams like T1, Gen.G, and DRX leading the charge @ As we move forward, it will be interesting to see how the standings evolve and which teams will rise to the occasion during the playoffs"),
                new ChatMessage(ASSISTANT, "In conclusion, the LCK continues to be a thrilling league filled with talent, competition, and passionate fans. @ The current season has been nothing short of exciting, with teams like T1, Gen.G, and DRX leading the charge @ As we move forward, it will be interesting to see how the standings evolve and which teams will rise to the occasion during the playoffs. @"),
                new ChatMessage(USER, script)
        );

        return getOpenAiResult(generatePrompt(systemPrompt, chatPrompt));
    }

    @PostConstruct
    private void init() {
        openAiService = new OpenAiService(SECRET_KEY, Duration.ofSeconds(30));
    }

    private ChatCompletionRequest generatePrompt(List<ChatMessage> systemPrompt, List<ChatMessage> chatPrompt){
        List<ChatMessage> promptMessage = new ArrayList<>();
        promptMessage.addAll(systemPrompt);
        promptMessage.addAll(chatPrompt);

        return ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(promptMessage)
                .temperature(0.2)
                .build();
    }

    private String getOpenAiResult(ChatCompletionRequest request){
        Long startTime = System.currentTimeMillis();
        ChatCompletionResult result = openAiService.createChatCompletion(request);
        Long endTime = System.currentTimeMillis();
        System.out.printf("GPTTranslationService: translation took %d seconds, consumed %d tokens total (prompt %d, completion %d)%n", (endTime-startTime)/1000, result.getUsage().getTotalTokens(), result.getUsage().getPromptTokens(), result.getUsage().getCompletionTokens());
        return result.getChoices().get(0).getMessage().getContent();
    }

}
