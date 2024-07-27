package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.dto.TTSDTO;
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

    public String createSpeech(TTSDTO ttsdto) {
        String url = "https://texttospeech.googleapis.com/v1beta1/text:synthesize?key="+apiKey;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("enableTimePointing", new String[]{"SSML_MARK"});

        Map<String, Object> input = new HashMap<>();
        input.put("ssml", "<speak><mark name=\"0\"/>I <mark name=\"1\"/>am <mark name=\"2\"/>my <mark name=\"3\"/>aunt's <mark name=\"4\"/>sister's <mark name=\"5\"/>daughter. <mark name=\"6\"/>He <mark name=\"7\"/>was <mark name=\"8\"/>sure <mark name=\"9\"/>the <mark name=\"10\"/>Devil <mark name=\"11\"/>created <mark name=\"12\"/>red <mark name=\"13\"/>sparkly <mark name=\"14\"/>glitter.</speak>");
        requestBody.put("input", input);

        Map<String, Object> voice = new HashMap<>();
        voice.put("name", ttsdto.getVoice());
        voice.put("languageCode", ttsdto.getLanguage());
        voice.put("ssmlGender",ttsdto.getGender());
        requestBody.put("voice", voice);

        Map<String, Object> audioConfig = new HashMap<>();
        audioConfig.put("audioEncoding", "MP3");
        requestBody.put("audioConfig", audioConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
