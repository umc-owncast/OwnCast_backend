package com.umc.owncast.domain.cast.service;

import com.umc.owncast.common.annotation.TrackExecutionTime;
import com.umc.owncast.domain.cast.dto.KeywordCastCreationDTO;
import com.umc.owncast.domain.cast.dto.TTSDTO;
import com.umc.owncast.domain.cast.dto.TTSResultDTO;
import com.umc.owncast.domain.cast.dto.TTSTempDTO;
import com.umc.owncast.domain.enums.VoiceCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TTSService {
    private static final Integer MaxByteLength = 4000;
    private final RestTemplate restTemplate;
    private final ParsingService parsingService;
    private final FileService fileService;

    @Value("${google.api.key}")
    private String apiKey;

    @TrackExecutionTime
    public TTSResultDTO createSpeech(String[] script, KeywordCastCreationDTO keywordCastCreationDTO) {
        List<Double> timePoints = new ArrayList<>();
        timePoints.add(0.0);
        byte[] audio = new byte[0];
        List<List<String>> splitScript = splitSentencesByByte(script);
        for (List<String> row : splitScript) {
            TTSTempDTO ttsTempDTO= requestSpeech(setSpeech(row, keywordCastCreationDTO));

            byte[] result = new byte[audio.length + ttsTempDTO.getAudioBytes().length];
            System.arraycopy(audio, 0, result, 0, audio.length);
            System.arraycopy(ttsTempDTO.getAudioBytes(), 0, result, audio.length, ttsTempDTO.getAudioBytes().length);
            audio = result;

            if(timePoints.isEmpty()) {
                timePoints = ttsTempDTO.getTimePointList();
            } else {
                Double lastValue = timePoints.get(timePoints.size() - 1);
                for(Double value : ttsTempDTO.getTimePointList()) {
                    timePoints.add(lastValue + value);
                }
                Collections.sort(timePoints);
            }
        }
        String path = makeAudioFile(audio);
        return TTSResultDTO.builder()
                .mp3Path(path)
                .timePointList(timePoints)
                .build();
    }

    private TTSDTO setSpeech(List<String> script, KeywordCastCreationDTO keywordCastCreationDTO) {
        String processedScript = parsingService.addMarks(script);
        String voice = VoiceCode.fromValue(keywordCastCreationDTO.getVoice()).getValue();
        return TTSDTO.builder()
                .voice(voice)   //ex: "en-US-Standard-A"
                .language(voice.substring(0, 5))
                .script(processedScript)
                .build();
    }

    private TTSTempDTO requestSpeech(TTSDTO ttsdto) {
        String url = "https://texttospeech.googleapis.com/v1beta1/text:synthesize?key=" + apiKey;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("enableTimePointing", new String[]{"SSML_MARK"});

        Map<String, Object> input = new HashMap<>();
        input.put("ssml", ttsdto.getScript());
        requestBody.put("input", input);

        Map<String, Object> voice = new HashMap<>();
        voice.put("name", ttsdto.getVoice());
        voice.put("languageCode", ttsdto.getLanguage());
        requestBody.put("voice", voice);

        Map<String, Object> audioConfig = new HashMap<>();
        audioConfig.put("audioEncoding", "MP3");
        requestBody.put("audioConfig", audioConfig);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        Long startTime = System.currentTimeMillis();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        Long endTime = System.currentTimeMillis();
        System.out.printf("TTSService: TTS took %.2f seconds%n", (double)(endTime - startTime)/1000);
        String audioContent = (String) response.getBody().get("audioContent");

        List<Map<String, Object>> timepoints = (List<Map<String, Object>>) response.getBody().get("timepoints");

        List<Double> timepointList = new ArrayList<>();
        for (Map<String, Object> timepoint : timepoints) {
            if (timepoint.containsKey("timeSeconds")) {
                timepointList.add(((Number) timepoint.get("timeSeconds")).doubleValue());
            }
        }

        byte[] audioBytes = Base64.getDecoder().decode(audioContent);

        return TTSTempDTO.builder()
                .audioBytes(audioBytes)
                .timePointList(timepointList)
                .build();
    }

    private String makeAudioFile (byte[] audioBytes) {
        MultipartFile multipartFile = new MockMultipartFile(
                UUID.randomUUID() + ".mp3",
                UUID.randomUUID() + ".mp3",
                "audio/mpeg",
                audioBytes);
        return fileService.uploadFile(multipartFile);
    }

    private List<List<String>> splitSentencesByByte(String[] sentences) {
        List<List<String>> result = new ArrayList<>();
        int i = 0;

        while(i < sentences.length)
        {
            List<String> row = new ArrayList<>();
            int len = 0;
            do {
                row.add(sentences[i]);
                len += sentences[i].getBytes(StandardCharsets.UTF_8).length;
                i++;
            } while (len < MaxByteLength && i < sentences.length);
            result.add(row);
        }

        return result;
    }
}
