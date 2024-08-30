package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.domain.enums.Language;
import org.springframework.stereotype.Service;

@Service
public interface TranslationService {
    /** script 한국어로 번역 */
    String translateToKorean(String script);
    String translateToMemberLanguage(String script, Language language);
}
