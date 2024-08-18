package com.umc.owncast.domain.sentence.service;

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
public class ChatGptTranslationService implements TranslationService{
    @Value("${open-ai.secret-key}")
    private String SECRET_KEY;
    private OpenAiService openAiService;

    private final String USER = ChatMessageRole.USER.value();
    private final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
    private final String SYSTEM = ChatMessageRole.SYSTEM.value();

    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(SECRET_KEY, Duration.ofSeconds(30)); // 30초 내에 응답 안올 시 예외 던짐
        // TODO openAiService 싱글톤으로 (OpenAiServiceWrapper 빈으로 감싸서 클래스 이곳 저곳에 제공?)
    }

    @Override
    public String translate(String script) {
        List<ChatMessage> systemPrompt = List.of(
                new ChatMessage(SYSTEM, "You are a translator which translates given script to korean."),
                new ChatMessage(SYSTEM, "Translation result should look natural"),
                new ChatMessage(SYSTEM, "'@' means end of the sentence; you should leave it be.")
        );

        List<ChatMessage> chatPrompt = List.of(
                new ChatMessage(USER, "Welcome to today's episode of our financial news podcast, where we delve into the latest developments in the S&P 500. "),
                new ChatMessage(ASSISTANT, "오늘의 팟캐스트에 오신 걸 환영합니다, 오늘은 가장 최근의 S&P 500 근황에 대해 알아보겠습니다."),
                new ChatMessage(USER, "A Farewell To Arms"),
                new ChatMessage(ASSISTANT, "무기여 잘 있거라"),
                new ChatMessage(USER, "Welcome, This is a test script! @ Thank you! @"),
                new ChatMessage(ASSISTANT, "환영합니다, 이건 테스트용 스크립트에요! @ 감사합니다! @"),
                new ChatMessage(USER, script)
        );

        return getOpenAiResult(generatePrompt(systemPrompt, chatPrompt));
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
