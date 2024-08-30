package com.umc.owncast.common.initializer;

import com.umc.owncast.OwncastApplication;
import com.umc.owncast.domain.cast.dto.CastScriptDTO;
import com.umc.owncast.domain.cast.dto.ScriptCastCreationDTO;
import com.umc.owncast.domain.cast.service.CastService;
import com.umc.owncast.domain.enums.Formality;
import com.umc.owncast.domain.enums.VoiceCode;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.member.repository.MemberRepository;
import com.umc.owncast.domain.voiceexample.entity.VoiceExample;
import com.umc.owncast.domain.voiceexample.repository.VoiceExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class VoiceExampleGenerator implements CommandLineRunner {
    private final VoiceExampleRepository voiceExampleRepository;
    private final MemberRepository memberRepository;
    private final CastService castService;

    /**
     * 엔트리 포인트 1
     * 어플리케이션 실행할 때마다 작동..
     * 굳이 VoiceExample을 매번 체크할 필요는 없고, 앱 실행시마다 작동시키기엔 오래걸릴 것 같지만
     * 아래 main()이 잘 작동하지 않으므로 일단 이걸로 갑시다
     * */
    @Override
    public void run(String... args) throws Exception {
        ㅊㅁ
    }

    /**
     * 엔트리 포인트 2
     * VoiceCode 바뀌거나, 예시 음성 바꿀 때만 main 실행
     * -> 근데 잘 작동 안 함..
     * */
    public static void main(String[] args) {
        // ApplicationContext에서 빈 주입
        ApplicationContext context = new AnnotationConfigApplicationContext(OwncastApplication.class);
        VoiceExampleRepository voiceExampleRepository = context.getBean(VoiceExampleRepository.class);
        MemberRepository memberRepository = context.getBean(MemberRepository.class);
        CastService castService = context.getBean(CastService.class);

        VoiceExampleGenerator self = new VoiceExampleGenerator(voiceExampleRepository,
                memberRepository,
                castService);
        self.initRepository();
        self.generate("Welcome to Owncast!");  // 예시 대사 : "Welcome to Owncast!" -> 나중에 바꿔도 됨
    }

    
    /** 주어진 대사로 오디오 생성해서 VoiceExample 테이블에 저장 */
    public void generate(final String PHRASE){
        System.out.println("VoiceExampleGenerator: Generating Voice Example...");
        final Member OWNCAST_MEMBER = memberRepository.findByLoginId("owncast").orElseThrow(); // owncast 계정으로 캐스트 생성

        for(VoiceCode voiceCode : VoiceCode.values()){
            generate(PHRASE, voiceCode, OWNCAST_MEMBER);
        }
    }

    /** 특정 목소리만 오디오 저장 */
    public void generate(final String PHRASE, VoiceCode voiceCode, Member member){
        // VoiceCode 마다 임시 캐스트 생성
        CastScriptDTO cast = castService.createCastByScript(
                ScriptCastCreationDTO.builder()
                        .voice(voiceCode.name())
                        .formality(Formality.OFFICIAL)
                        .script(PHRASE)
                        .build(),
                member);

        // 오디오 파일 VoiceExample에 저장
        VoiceExample voiceExample = voiceExampleRepository.findByVoiceCode(voiceCode)
                .orElseGet(() -> {
                    System.out.println("- VoiceExample of " + voiceCode + " not found");
                    return VoiceExample.builder()
                            .voiceCode(voiceCode)
                            .build();});
        voiceExample.update(cast.getFileUrl());
        voiceExampleRepository.save(voiceExample);
        System.out.println("- VoiceExample of " + voiceExample.getVoiceCode() + " saved (url=" + voiceExample.getFilePath() + ")");
        // 임시 캐스트 DB에서 삭제
        castService.deleteCast(cast.getId(), member);
    }

    /** voice_example 테이블 초기화 -> enum으로는 존재하는데 테이블에는 없는 목소리 저장 */
    private void initRepository(){
        System.out.println("VoiceExampleGenerator: initializing repository...");
        // DB에 저장되지 않은 VoiceCode 구해서 저장
        List<VoiceExample> saved = voiceExampleRepository.findAll();
        Set<VoiceCode> notSaved = new HashSet<>(Set.of(VoiceCode.values()));
        saved.forEach(item -> {
                    notSaved.remove(item.getVoiceCode());
                });
        
        notSaved.forEach(item -> {
            System.out.println("- saved " + item.name());
            voiceExampleRepository.save(VoiceExample.builder()
                            .voiceCode(item)
                            .filePath(null)
                            .build());
        });
    }
}
