package com.umc.owncast.domain.cast.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGPTPromptGenerator {
    /* GPT 모델 : "gpt-4o" 혹은 "gpt-4o-mini" 중 선택 */
    private final String DEFAULT_MODEL = "gpt-4o-mini";

    private final double DEFAULT_TEMPERATURE = 0.1;

    public ChatCompletionRequest generatePrompt(String keyword) {
        return generatePrompt(keyword, DEFAULT_MODEL);
    }

    public ChatCompletionRequest generatePrompt(String keyword, String modelName) {
        List<ChatMessage> promptMessage = createPromptMessage(keyword);

        ChatCompletionRequest prompt = ChatCompletionRequest.builder()
                .model(modelName)
                .messages(promptMessage)
                .temperature(DEFAULT_TEMPERATURE)
                .build();

        System.out.println("ChatGPTPromptGenerator - generated prompt:");
        System.out.println(prompt);
        return prompt;
    }

    public List<ChatMessage> createPromptMessage(String keyword) {
        // 현재 사용자의 언어 설정에 맞춘다 TODO: 회원 기능으로 언어 설정 가져오기
        String language = "Korean";
        return createPromptMessage(keyword, language);
    }

    public List<ChatMessage> createPromptMessage(String keyword, String language) {
        List<ChatMessage> systemPrompts;
        List<ChatMessage> chatPrompts;

        // 시스템 프롬프트
        String system = ChatMessageRole.SYSTEM.value();
        systemPrompts = List.of(
                new ChatMessage(system, "You should give answers that are popular to people"), // 역할 부여
                new ChatMessage(system, "Answer should only contain what you have to say (no markdowns or background musics)"),
                new ChatMessage(system, "Answer should be familiar to people and recent keywords"),
                new ChatMessage(system, "The answer should be in " + language.toLowerCase()) // 언어 설정 (English, Spanish, Japanese, ...)
        );

        // 채팅 메시지
        String user = ChatMessageRole.USER.value();
        String assistant = ChatMessageRole.ASSISTANT.value();

        chatPrompts = List.of(
                new ChatMessage(user, "Give me six keywords in array format about " + keyword)
        );

        // 반환
        List<ChatMessage> result = new ArrayList<>();
        result.addAll(systemPrompts);
        result.addAll(chatPrompts);
        return result;
    }
}
