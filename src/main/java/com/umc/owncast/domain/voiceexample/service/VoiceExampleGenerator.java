package com.umc.owncast.domain.voiceexample.service;

import com.umc.owncast.OwncastApplication;
import com.umc.owncast.domain.enums.VoiceCode;
import com.umc.owncast.domain.voiceexample.repository.VoiceExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoiceExampleGenerator {
    private final VoiceExampleRepository voiceExampleRepository;

    /** 특정한 PHRASE로 예시 음성을 생성하여 목소리에 매치한다. */
    public void generate(final String PHRASE){
        
    }

    /** 특정 목소리에 지정한 PHRASE로 예시 음성을 생성한다. */
    public void generate(final String PHRASE, VoiceCode voice){

    }

    /** generate() 함수 실행 */
    public static void main(String[] args) {
        // Spring Boot ApplicationContext를 생성하여 빈을 관리하도록 설정
        ApplicationContext context = new AnnotationConfigApplicationContext(OwncastApplication.class);

        // ApplicationContext에서 원하는 빈을 가져옵니다.
        VoiceExampleRepository voiceExampleRepository = context.getBean(VoiceExampleRepository.class);
        VoiceExampleGenerator g = new VoiceExampleGenerator(voiceExampleRepository);
        g.generate("Welcome to Owncast!");
    }
}
