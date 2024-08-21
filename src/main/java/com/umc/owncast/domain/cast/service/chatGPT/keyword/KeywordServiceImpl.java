package com.umc.owncast.domain.cast.service.chatGPT.keyword;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGptAnswerGenerator;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGptPromptGenerator;
import com.umc.owncast.domain.category.repository.MainCategoryRepository;
import com.umc.owncast.domain.category.repository.SubCategoryRepository;
import com.umc.owncast.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final ChatGptPromptGenerator chatGPTPromptGenerator;
    private final ChatGptAnswerGenerator answerGenerator;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public List<String> createKeyword(Member member) {

        String script = "";
        List<String> keywords = null;

        String mainCategoryName = mainCategoryRepository.getMainCategoryNameByMember(member).orElseThrow(()->new UserHandler(ErrorCode.CATEGORY_NOT_EXIST));
        String subCategoryName = subCategoryRepository.getSubCategoryNameByMember(member).orElseThrow(()->new UserHandler(ErrorCode.CATEGORY_NOT_EXIST));

        if(mainCategoryName.equals("직접 입력")) mainCategoryName = subCategoryName;

        try {
            ChatCompletionRequest prompt = chatGPTPromptGenerator.generateKeywordPrompt(mainCategoryName, subCategoryName);
            script = answerGenerator.generateAnswer(prompt);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            keywords = gson.fromJson(script, listType);

        } catch (Exception e) {
            System.out.println("CastServiceImpl: Exception on createScript - " + e.getMessage());
            System.out.println("Exception class : " + e.getClass());
            System.out.println("Exception cause : " + e.getCause());
            throw e;
        }
        return keywords;
    }
}
