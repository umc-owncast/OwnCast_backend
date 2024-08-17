package com.umc.owncast.domain.cast.service.chatGPT.keyword;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGPTAnswerGenerator;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGPTPromptGenerator;
import com.umc.owncast.domain.category.repository.MainCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final ChatGPTPromptGenerator chatGPTPromptGenerator;
    private final ChatGPTAnswerGenerator answerGenerator;
    private final MainCategoryRepository mainCategoryRepository;

    @Override
    public List<String> createKeyword() {
        String script = "";
        List<String> keywords = null;

        // 멤버 토큰으로 id 가져오기
        Optional<String> optionalCategoryName = mainCategoryRepository.getMainCategoryNameByMemberId(2L);
        String categoryName = null;

        if (optionalCategoryName.isEmpty()) {
            throw new UserHandler(ErrorCode._BAD_REQUEST);
        } else {
            categoryName = optionalCategoryName.get();
        }

        try {
            ChatCompletionRequest prompt = chatGPTPromptGenerator.generateKeywordPrompt(categoryName);
            script = answerGenerator.generateAnswer(prompt);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            keywords = gson.fromJson(script, listType);

        } catch (Exception e) {
            // 출력만 하고 전파 -> CastService에서 처리??
            System.out.println("CastServiceImpl: Exception on createScript - " + e.getMessage());
            System.out.println("Exception class : " + e.getClass());
            System.out.println("Exception cause : " + e.getCause());
            throw e;
        }
        return keywords;
    }
}
