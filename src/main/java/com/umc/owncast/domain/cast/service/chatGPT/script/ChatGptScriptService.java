package com.umc.owncast.domain.cast.service.chatGPT.script;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.umc.owncast.common.annotation.TrackExecutionTime;
import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGptAnswerGenerator;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGptPromptGenerator;
import com.umc.owncast.domain.sentence.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatGptScriptService implements ScriptService {

    private final ChatGptPromptGenerator promptGenerator;
    private final ChatGptAnswerGenerator answerGenerator;
    private final TranslationService translationService;

    @TrackExecutionTime
    public String createScript(Member member, KeywordCastCreationDTO castRequest) {
        String script = "";
        try {
            String translatedKeyword = translationService.translateToMemberLanguage(castRequest.getKeyword(), member.getLanguage());
            castRequest.setKeyword(translatedKeyword);
            ChatCompletionRequest prompt = promptGenerator.generatePrompt(
                    castRequest.getKeyword(),
                    castRequest.getFormality(),
                    castRequest.getAudioTime(), member
            );
            script = answerGenerator.generateAnswer(prompt);
        } catch (Exception e) {
            // 출력만 하고 전파 -> CastService에서 처리??
            System.out.println("CastServiceImpl: Exception on createScript - " + e.getMessage());
            System.out.println("Exception class : " + e.getClass());
            System.out.println("Exception cause : " + e.getCause());
            throw e;
        }
        return script;
    }
}
