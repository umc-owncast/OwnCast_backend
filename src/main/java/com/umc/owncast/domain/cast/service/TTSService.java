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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TTSService {
    private static final Integer MaxByteLength = 4000; // 구글 api 바이트 제한
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
        // API 바이트 제한에 맞도록 script 분할하여 각자 API 요청
        List<List<String>> splitScript = splitSentencesByByte(script, MaxByteLength);
        for (List<String> row : splitScript) {
            // todo 이 부분도 어떻게 병렬화 가능할 거 같은데
            // API 요청
            TTSTempDTO ttsTempDTO = requestSpeech(setSpeech(row, keywordCastCreationDTO));

            // 응답 mp3 파일 처리
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

    /**
     * byte 파일을 mp3로 업로드한 후 url 반환
     * @param audioBytes mp3 파일
     * @return 파일 url
     * */
    private String makeAudioFile (byte[] audioBytes) {
        MultipartFile multipartFile = new MockMultipartFile(
                UUID.randomUUID() + ".mp3",
                UUID.randomUUID() + ".mp3",
                "audio/mpeg",
                audioBytes);
        return fileService.uploadFile(multipartFile);
    }

    /**
     * 주어진 문자열 배열을 byteLength를 초과하지 않도록 분할하여 리스트에 담아 반환
     * @param sentences 분할할 문자열 배열
     * @param byteLength 바이트 제한
     * @return byteLength를 넘지 않도록 분할한 문자열 리스트 (리스트 각 요소는 byteLength를 넘지 않음)
     * */
    private List<List<String>> splitSentencesByByte(String[] sentences, int byteLength) {
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
            } while (len < byteLength && i < sentences.length);
            result.add(row);
        }

        return result;
    }
}
