package com.umc.owncast.domain.sentence.service;

import org.springframework.stereotype.Service;

@Service
public interface TranslationService {
    public String translate(String script);
}
