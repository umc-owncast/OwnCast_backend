package com.umc.owncast.domain.cast.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatGPTScriptService implements ScriptService{

    private final ChatGPTPromptGenerator promptGenerator;
    private final ChatGPTScriptGenerator scriptGenerator;

    public String createScript(CastCreationRequestDTO castRequest) {
        try {
            ChatCompletionRequest prompt = promptGenerator.generatePrompt(
                    castRequest.getKeyword(),
                    castRequest.getFormality(),
                    castRequest.getAudioTime()
            );
            return scriptGenerator.generateScript(prompt);
        } catch(Exception e){
            // 출력만 하고 전파 -> CastService에서 처리??
            System.out.println("CastServiceImpl: Exception on createScript - " + e.getMessage());
            System.out.println("Exception class : " + e.getClass());
            System.out.println("Exception cause : " + e.getCause());
            throw e;
        }
    }
}
