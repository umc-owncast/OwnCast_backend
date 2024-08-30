package com.umc.owncast.domain.voiceexample.service;

import com.umc.owncast.common.exception.handler.UserHandler;
import com.umc.owncast.common.response.status.ErrorCode;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.voiceexample.dto.VoiceExampleDTO;
import com.umc.owncast.domain.voiceexample.entity.VoiceExample;
import com.umc.owncast.domain.voiceexample.repository.VoiceExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VoiceExampleService {
    private final VoiceExampleRepository voiceExampleRepository;

    public List<VoiceExampleDTO> fetchVoiceExamplesForMember(Member member) {
        if(Objects.isNull(member)) throw new UserHandler(ErrorCode.NOT_LOGGED_IN);
        Language language = member.getLanguage();
        String languageCode = language.getRealLanguage().substring(0, 2);
        String pronunciation = switch(language){
            case US -> "US";
            case UK -> "GB";
            case AUS -> "AU";
            case IND -> "IN";
            case JA -> "JP";
            case ES, ES_US -> "ES";
        };
        List<VoiceExample> voices = voiceExampleRepository.findByLanguageCodeAndPronunciation(languageCode, pronunciation);
        return voices.stream()
                .map(VoiceExampleDTO::new)
                .toList();
    }
}
