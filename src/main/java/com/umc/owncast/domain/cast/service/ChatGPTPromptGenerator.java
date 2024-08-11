package com.umc.owncast.domain.cast.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.umc.owncast.domain.enums.Formality;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/* 사용자 입력 정보를 바탕으로 ChatGPT에 건넬 프롬프트를 생성 */
public class ChatGPTPromptGenerator {

    /* GPT 모델 : "gpt-4o" 혹은 "gpt-4o-mini" 중 선택 */
    private final String DEFAULT_MODEL = "gpt-4o-mini";
    
    /* 문장을 뭘로 나눌지 */
    private final String SENTENCE_DELIMITER = "@";
    /**
    * 스크립트 결정성 제어변수, 0~2 사이값
    * 0에 가까울 수록 결정적인 답변을 얻음 (사실적)
    * 2에 가까울 수록 랜덤하고 다양한 답변을 얻음 (아무말대잔치)
    **/
    private final double DEFAULT_TEMPERATURE = 0.1;

    /* 사용자의 keyword를 바탕으로 프롬프트 생성 */
    public ChatCompletionRequest generatePrompt(String keyword, Formality formality, int audioTime) {
        return generatePrompt(keyword, formality, audioTime, DEFAULT_MODEL);
    }

    public ChatCompletionRequest generatePrompt(String keyword, Formality formality, int audioTime, String modelName) {
        List<ChatMessage> promptMessage = createPromptMessage(keyword, formality, audioTime);

        ChatCompletionRequest prompt = ChatCompletionRequest.builder()
                .model(modelName)
                .messages(promptMessage)
                .temperature(DEFAULT_TEMPERATURE)
                .build();

        System.out.println("ChatGPTPromptGenerator - generated prompt:");
        System.out.println(prompt);
        return prompt;
    }

    public List<ChatMessage> createPromptMessage(String keyword, Formality formality, int audioTime) {
        // 현재 사용자의 언어 설정에 맞춘다 TODO: 회원 기능으로 언어 설정 가져오기
        String language = "English";
        return createPromptMessage(keyword, formality, audioTime, language);
    }

    public List<ChatMessage> createPromptMessage(String keyword, Formality formality, int audioTime, String language) {
        List<ChatMessage> systemPrompts;
        List<ChatMessage> chatPrompts;

        // 시스템 프롬프트
        String system = ChatMessageRole.SYSTEM.value();
        systemPrompts = List.of(
                new ChatMessage(system, "You are the host of the podcast."), // 역할 부여
                new ChatMessage(system, "Answer should only contain what you have to say (no markdowns or background musics)"), // 형식 지정
                //new ChatMessage(system, "Answer should be less than " + tokens + " tokens"), // 분량 설정
                //new ChatMessage(system, "Use around " + words + "words"), // 분량 설정 2   -->  (calculateWords() 개발 후 해보기)
                new ChatMessage(system, "Answer should be " + audioTime/60 + " minutes long."), // 분량 설정 3
                new ChatMessage(system, "You should add " + SENTENCE_DELIMITER + " at each end of sentences."), // 문장 분리
                new ChatMessage(system, "Answer in " + formality.name() + " manner."), // 격식 설정 (official, casual)
                new ChatMessage(system, "The answer should be in " + language.toLowerCase()) // 언어 설정 (English, Spanish, Japanese, ...)
        );

        // 채팅 메시지
        String user = ChatMessageRole.USER.value();
        String assistant = ChatMessageRole.ASSISTANT.value();
        chatPrompts = List.of(
                new ChatMessage(user, "Make a podcast script about " + keyword)
        );

        // 반환
        List<ChatMessage> result = new ArrayList<>();
        result.addAll(systemPrompts);
        result.addAll(chatPrompts);
        return result;
    }

    /** 분당 단어 수 기반으로 오디오 분량에 맞는 단어 수 어림계산 (아직 개발 X) */
    private int calculateWords(int audioTime) {
        // TODO: 언어 사투리 별로 WPM 나눠 계산
        // WPM: Word Per Minute
        final int ENGLISH_WPM = 150;
        final int JAPANESE_WPM = 240;
        final int SPANISH_WPM = 260;

        final int WPM = 0;
        // TODO: 사용자 언어 설정에 따라 WPM 결정
        final String userLanguage = "TODO";
        switch(userLanguage.toLowerCase()){
            case "english":
                break;
            case "spanish":
                break;
            case "japanese":
                break;
            default:
                break;
        }
        return WPM * (audioTime/60);
    }

    public ChatCompletionRequest generateKeywordPrompt(String categoryName) {
        return generateKeywordPrompt(categoryName, DEFAULT_MODEL);
    }

    public ChatCompletionRequest generateKeywordPrompt(String categoryName, String modelName) {
        List<ChatMessage> promptMessage = createKeywordPromptMessage(categoryName);

        ChatCompletionRequest prompt = ChatCompletionRequest.builder()
                .model(modelName)
                .messages(promptMessage)
                .temperature(DEFAULT_TEMPERATURE)
                .build();

        System.out.println("ChatGPTPromptGenerator - generated prompt:");
        System.out.println(prompt);
        return prompt;
    }

    public List<ChatMessage> createKeywordPromptMessage(String keyword) {
        // 현재 사용자의 언어 설정에 맞춘다 TODO: 회원 기능으로 언어 설정 가져오기
        String language = "Korean";
        return createKeywordPromptMessage(keyword, language);
    }

    public List<ChatMessage> createKeywordPromptMessage(String keyword, String language) {
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
