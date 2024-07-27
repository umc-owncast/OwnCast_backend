package com.umc.owncast.domain.cast.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TTSService {
    private final RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String apiKey;

    public String createSpeech() {
        String url = "https://texttospeech.googleapis.com/v1beta1/text:synthesize?key="+apiKey; // 실제 API URL로 변경하세요

        // 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("enableTimePointing", new String[]{"SSML_MARK"});

        Map<String, Object> input = new HashMap<>();
        input.put("ssml", "<speak><mark name=\"0\"/>I <mark name=\"1\"/>am <mark name=\"2\"/>my <mark name=\"3\"/>aunt's <mark name=\"4\"/>sister's <mark name=\"5\"/>daughter. <mark name=\"6\"/>He <mark name=\"7\"/>was <mark name=\"8\"/>sure <mark name=\"9\"/>the <mark name=\"10\"/>Devil <mark name=\"11\"/>created <mark name=\"12\"/>red <mark name=\"13\"/>sparkly <mark name=\"14\"/>glitter.</speak>");
        requestBody.put("input", input);

        Map<String, Object> voice = new HashMap<>();
        voice.put("name", "en-US-Standard-A");
        voice.put("languageCode", "en-US");
        requestBody.put("voice", voice);

        Map<String, Object> audioConfig = new HashMap<>();
        audioConfig.put("audioEncoding", "MP3");
        requestBody.put("audioConfig", audioConfig);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 엔티티 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // API 요청
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 본문 반환
        return response.getBody();
    }
}
