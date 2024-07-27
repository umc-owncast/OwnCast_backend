package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.domain.cast.service.TTSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tts")
public class TTSController {
    private final TTSService ttsService;

    @PostMapping("/testtt")
    public String test() {
        return ttsService.createSpeech();
    }
}