package com.umc.owncast.domain.cast.service.chatGPT;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.umc.owncast.domain.enums.Formality;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.entity.Member;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/* 사용자 입력 정보를 바탕으로 ChatGPT에 건넬 프롬프트를 생성 */
public class ChatGptPromptGenerator {

    /* GPT 모델 : "gpt-4o" 혹은 "gpt-4o-mini" 중 선택 */
    private final String DEFAULT_MODEL = "gpt-4o";

    /* 문장을 뭘로 나눌지 */
    private final String SENTENCE_DELIMITER = "@";
    /**
     * 스크립트 결정성 제어변수, 0~2 사이값
     * 0에 가까울 수록 결정적인 답변을 얻음 (사실적)
     * 2에 가까울 수록 랜덤하고 다양한 답변을 얻음 (아무말대잔치)
     **/
    private final double DEFAULT_TEMPERATURE = 0.1;

    /* 사용자의 keyword를 바탕으로 프롬프트 생성 */
    public ChatCompletionRequest generatePrompt(String keyword, Formality formality, int audioTime, Member member) {
        return generatePrompt(keyword, formality, audioTime, DEFAULT_MODEL, member);
    }

    public ChatCompletionRequest generatePrompt(String keyword, Formality formality, int audioTime, String modelName, Member member) {
        List<ChatMessage> promptMessage = createPromptMessage(keyword, formality, audioTime, member);

        ChatCompletionRequest prompt = ChatCompletionRequest.builder()
                .model(modelName)
                .messages(promptMessage)
                .temperature(DEFAULT_TEMPERATURE)
                .build();

        System.out.println("ChatGptPromptGenerator - generated prompt:");
        System.out.println(prompt);
        return prompt;
    }

    public List<ChatMessage> createPromptMessage(String keyword, Formality formality, int audioTime, Member member) {
        // 현재 사용자의 언어 설정에 맞춘다
        String language = member.getLanguage().getRealLanguage();
        List<ChatMessage> systemPrompts;
        List<ChatMessage> chatPrompts;

        // 시스템 프롬프트
        final String SYSTEM = ChatMessageRole.SYSTEM.value();
        systemPrompts = List.of(
                /* 예전 프롬프트
                new ChatMessage(SYSTEM, "You are the host of the podcast."),
                new ChatMessage(SYSTEM, "Your job is to make a podcast script about what happened recently. It is best if you say things based on real news"),
                new ChatMessage(SYSTEM, "script should only contain what you have to say (no markdowns or background musics)"),
                new ChatMessage(SYSTEM, "Don't write .! except at the end of a sentence (not Dr. or Mr. or Miss,, use Doctor, Mister, and Miss)"),
                new ChatMessage(SYSTEM, "Answer in " + formality.name().toLowerCase() + " manner."),
                new ChatMessage(SYSTEM, "Answer in " + member.getLanguage().getRealLanguage() + ".")
                 */
                new ChatMessage(SYSTEM, "You are the host of the podcast, named 'OwncastBot'"),
                new ChatMessage(SYSTEM, "Your job is to write a podcast script about what happened recently; Write it based on real news."),
                new ChatMessage(SYSTEM, "Script should only contain what you have to say (no markdowns or background musics)"),
//                new ChatMessage(SYSTEM, "Seperate each sentence using '@'."),
                new ChatMessage(SYSTEM, "Answer in " + formality.name().toLowerCase() + " manner."),
                new ChatMessage(SYSTEM, "Answer in " + member.getLanguage().getRealLanguage() + ".")
        );

        // 채팅 메시지
        final String USER = ChatMessageRole.USER.value();
        final String ASSISTANT = ChatMessageRole.ASSISTANT.value();
        chatPrompts = List.of(
                new ChatMessage(USER, "Make a " + String.format("%.1f", audioTime/60f) + " minute podcast script about " + keyword + "; Use around " + ChatGptPromptGenerator.calculateWords(audioTime, member) + " words.")
        );

        // 반환
        List<ChatMessage> result = new ArrayList<>();
        result.addAll(systemPrompts);
        result.addAll(chatPrompts);
        return result;
    }

    /**
     * 분당 단어 수 기반으로 오디오 분량에 맞는 단어 수 어림계산 (아직 개발 X)
     */
    static int calculateWords(int audioTime, Member member) {
        // WPM: Word Per Minute
        int WPM = switch (member.getLanguage()) {
            case US -> 140;
            case UK -> 190;
            case AUS -> 190;
            case IND -> 170;
            case JA -> 150;
            case ES -> 200;
            case ES_US -> 200;
            default -> 150;
        };
        return (int) (WPM *  (audioTime / 60f));
    }

    public ChatCompletionRequest generateKeywordPrompt(String mainCategoryName, String subCategoryName) {
        return generateKeywordPrompt(mainCategoryName, subCategoryName, DEFAULT_MODEL);
    }

    public ChatCompletionRequest generateKeywordPrompt(String mainCategoryName, String subCategoryName, String modelName) {
        List<ChatMessage> promptMessage = createKeywordPromptMessage(mainCategoryName, subCategoryName);

        ChatCompletionRequest prompt = ChatCompletionRequest.builder()
                .model(modelName)
                .messages(promptMessage)
                .temperature(DEFAULT_TEMPERATURE)
                .build();

        return prompt;
    }

    public List<ChatMessage> createKeywordPromptMessage(String keyword1, String keyword2) {
        String language = "Korean";
        return createKeywordPromptMessage(keyword1, keyword2, language);
    }

    public List<ChatMessage> createKeywordPromptMessage(String keyword1, String keyword2,String language) {
        List<ChatMessage> systemPrompts;
        List<ChatMessage> chatPrompts;

        // 시스템 프롬프트
        String system = ChatMessageRole.SYSTEM.value();
        systemPrompts = List.of(
                new ChatMessage(system, "The answer should only contain the information you need to convey (no markdowns or background music)."),
                new ChatMessage(system, "Keywords should not be too generic; instead, they should include specific terms, names of people, team names, specific events, or technical terms that are currently popular."),
                new ChatMessage(system, "Update the words to reflect the latest trends and current prominent figures or events."),
                new ChatMessage(system, "Provide 6 unique words related to each of the following keywords, ensuring that they are relevant to the input keyword. Adapt to current trends and popular terms or figures in each category: News, Food, Books, Drama/Movies, Art, Sports, Music. For example:"),
                new ChatMessage(system, "For News: Use terms like 'AI breakthroughs,' 'Social media regulations,' 'Global warming debates,' 'Political scandals,' 'Tech company lawsuits,' 'Emerging economies,' but ensure they relate to the input keyword."),
                new ChatMessage(system, "For Food: Include trends such as 'Vegan cheese,' 'Craft cocktails,' 'Fusion cuisine,' 'Plant-based meat,' 'Sourdough starter,' 'Artisanal ice cream,' but ensure they relate to the input keyword."),
                new ChatMessage(system, "For Books: Use recent influential works or authors like 'The Invisible Life of Addie LaRue,' 'A Promised Land,' 'Educated,' 'The Tattooist of Auschwitz,' 'Circe,' 'Normal People,' but ensure they relate to the input keyword."),
                new ChatMessage(system, "For Drama/Movies: Include contemporary films or shows like 'Dune Part 2,' 'The Last of Us,' 'Spider-Man: Across the Spider-Verse,' 'Succession Season 4,' 'The Whale,' 'Avatar: The Way of Water,' but ensure they relate to the input keyword."),
                new ChatMessage(system, "For Art: Include recent art trends or notable figures like 'Beeple,' 'NFT art,' 'Yayoi Kusama,' 'Banksy's new works,' 'Virtual reality art,' 'Documenta 15,' but ensure they relate to the input keyword."),
                new ChatMessage(system, "For Sports: Use current trends or figures such as 'Live betting,' 'Esports tournaments,' 'Neymar Jr.,' 'Formula 1 regulations,' '2024 Olympics,' 'Team USA basketball,' but ensure they relate to the input keyword."),
                new ChatMessage(system, "For Music: Include popular or emerging artists and trends like 'Bad Bunny,' 'Lo-fi beats,' 'Music NFTs,' 'Adele's latest album,' 'Travis Scott collaborations,' 'Billboard Hot 100 hits,' but ensure they relate to the input keyword."),
                new ChatMessage(system, "Make sure that the 6 words provided are related to both the primary keyword and the specific category."),
                new ChatMessage(system, "The answer should be in " + language.toLowerCase())  // Language setting (English, Spanish, Japanese, etc.)
        );

        // 채팅 메시지
        String user = ChatMessageRole.USER.value();
        String assistant = ChatMessageRole.ASSISTANT.value();

        chatPrompts = List.of(
                new ChatMessage(user, "Give me six keywords in one array format about " + keyword1 + "and" + keyword2 + "that are more related to second keyword")
        );

        // 반환
        List<ChatMessage> result = new ArrayList<>();
        result.addAll(systemPrompts);
        result.addAll(chatPrompts);
        return result;
    }


}
