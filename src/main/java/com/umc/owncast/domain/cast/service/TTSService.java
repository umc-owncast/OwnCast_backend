package com.umc.owncast.domain.cast.service;

import com.umc.owncast.domain.cast.dto.CastCreationRequestDTO;
import com.umc.owncast.domain.cast.dto.TTSDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TTSService {
    private final RestTemplate restTemplate;
    private final ParsingService parsingService;
    private final FileService fileService;

    @Value("${google.api.key}")
    private String apiKey;

    public String createSpeech(String script, CastCreationRequestDTO castCreationRequestDTO) {
        return requestSpeech(setSpeech(script, castCreationRequestDTO));
    }

    private TTSDTO setSpeech(String script, CastCreationRequestDTO castCreationRequestDTO) {
        String[] seperatedSentences = parsingService.parseSentences(script);
        String processedScript = parsingService.addMarks(seperatedSentences);
        return TTSDTO.builder()
                .voice(castCreationRequestDTO.getVoice())   //ex: "en-US-Standard-A"
                .language(castCreationRequestDTO.getVoice().substring(0, 5))
                .script(processedScript)
                .build();
    }
    private String requestSpeech(TTSDTO ttsdto) {
        String url = "https://texttospeech.googleapis.com/v1beta1/text:synthesize?key="+apiKey;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("enableTimePointing", new String[]{"SSML_MARK"});

        Map<String, Object> input = new HashMap<>();
        input.put("ssml", ttsdto.getScript());
        requestBody.put("input", input);

        Map<String, Object> voice = new HashMap<>();
        voice.put("name", ttsdto.getVoice());
        voice.put("languageCode", "en-US");
        requestBody.put("voice", voice);

        Map<String, Object> audioConfig = new HashMap<>();
        audioConfig.put("audioEncoding", "MP3");
        requestBody.put("audioConfig", audioConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        String audioContent = (String) response.getBody().get("audioContent");

        byte[] audioBytes = Base64.getDecoder().decode(audioContent);

        MultipartFile multipartFile = new MockMultipartFile(
                UUID.randomUUID() + ".mp3",
                UUID.randomUUID() + ".mp3",
                "audio/mpeg",
                audioBytes);
        return fileService.uploadImage(multipartFile);
    }
}
