package com.umc.owncast.domain.cast.service.chatGPT;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ChatGptAnswerGenerator {
    @Value("${open-ai.secret-key}")
    private String SECRET_KEY;
    private OpenAiService openAiService;

    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(SECRET_KEY, Duration.ofSeconds(45)); // 30초 내에 응답 안올 시 예외 던짐
    }

    /**
     * API를 요청해서 답변을 가져온다  <br>
     * 프롬프트에 따라 여러가지 답변이 오게 만들 수도 있는데, <br>
     * 이 경우 여러가지 Choice 중 첫 번째의 답변을 반환한다
     */
    public String generateAnswer(ChatCompletionRequest request) {
        Long startTime = System.currentTimeMillis();
        ChatCompletionResult result = null;
        result = openAiService.createChatCompletion(request); // OpenAiHttpException 발생 가능
        Long endTime = System.currentTimeMillis();
        // 첫 번째 Choice의 답변 반환
        ChatCompletionChoice targetChoice = result.getChoices().get(0);
        System.out.printf("ChatGPTAnswerGenerator: generation took %.2f seconds, consumed %d tokens total (prompt %d, completion %d)%n"
                , (double)(endTime - startTime)/1000, result.getUsage().getTotalTokens(), result.getUsage().getPromptTokens(), result.getUsage().getCompletionTokens());
        return targetChoice.getMessage().getContent();
    }
}
