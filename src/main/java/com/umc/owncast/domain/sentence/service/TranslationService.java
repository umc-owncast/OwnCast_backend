package com.umc.owncast.domain.sentence.service;

import org.springframework.stereotype.Service;

@Service
public interface TranslationService {
    /** script 한국어로 번역 */
    public String translate(String script);
}
