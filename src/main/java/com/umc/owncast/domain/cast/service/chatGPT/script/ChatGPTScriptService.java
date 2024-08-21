package com.umc.owncast.domain.cast.service.chatGPT.script;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGPTAnswerGenerator;
import com.umc.owncast.domain.cast.service.chatGPT.ChatGPTPromptGenerator;
import com.umc.owncast.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatGPTScriptService implements ScriptService {

    private final ChatGPTPromptGenerator promptGenerator;
    private final ChatGPTAnswerGenerator answerGenerator;

    public String createScript(Member member, KeywordCastCreationDTO castRequest) {
        String script = "";
        try {
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
