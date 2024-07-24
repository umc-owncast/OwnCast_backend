package com.umc.owncast.domain.cast.controller;

import com.umc.owncast.domain.cast.service.TTSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tts")
public class TTSController {
    private final TTSService ttsService;

    @CrossOrigin
    @GetMapping("/testtt")
    public void test() throws Exception {
        ttsService.createSpeech("testing script. input will be passed by whole script. So we have to make the function that converts sentences into whole script.");
    }
}