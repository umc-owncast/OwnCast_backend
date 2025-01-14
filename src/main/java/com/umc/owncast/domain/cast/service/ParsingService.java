package com.umc.owncast.domain.cast.service;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParsingService {
    private static final int MAX_LENGTH = 220;
    private final SentenceDetectorME sentenceDetector;

    public ParsingService() throws IOException {
        // 문장 감지 모델 로드
        InputStream modelIn = getClass().getResourceAsStream("/models/en-sent.bin");
        SentenceModel model = new SentenceModel(modelIn);
        this.sentenceDetector = new SentenceDetectorME(model);
    }

    public String placeDelimiter(String script) {
        String[] sentences = sentenceDetector.sentDetect(script);

        // 결과 문자열 생성
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sentences.length; i++) {
            result.append(sentences[i].trim());
            if (i < sentences.length - 1) {
                result.append("@");
            }
        }

        return result.toString();
    }

    public String[] parseSentencesByDelimiter(String script){
        String[] sentences = script.split("@|\\n\\n");
        List<String> result = new ArrayList<>();
        for(String s : sentences){
            if(s.isBlank()) continue;
            result.add(s.strip());
        }
        return result.toArray(new String[0]);
    }

    /** 구두점 (.!?)으로 문장 파싱 <br>
     * => 예외상황 발생하므로 사용 X <br>
     * (구두점 기준 파싱 필요해지면 Apache OpenNLP 등 라이브러리 사용하도록 변경) */
    @Deprecated
    public String[] parseSentencesByPunctuation(String script) {
        Pattern pattern = Pattern.compile("[^.!?@]+[.!?@]?");
        List<String> sentences = new ArrayList<>();
        Matcher matcher = pattern.matcher(script);

        while (matcher.find()) {
            String sentence = matcher.group();
            sentence = sentence.replace("@", "").strip();

            if (!sentence.isEmpty()) {
                sentences.add(sentence);
            }
        }

        return sentences.toArray(new String[0]);

    }

    public String addMarks(List<String> sentences) {
        int i = 0;
        StringBuilder processedScript = new StringBuilder("<speak>");

        for (String sentence : sentences) {
            if(sentence.length() > MAX_LENGTH) {
                while(sentence.length() > MAX_LENGTH) {
                    int splitIndex = sentence.lastIndexOf(" ", MAX_LENGTH);
                    if (splitIndex == -1) {
                        splitIndex = MAX_LENGTH;
                    }
                    processedScript.append(sentence, 0, splitIndex).append("\n");
                    sentence = sentence.substring(splitIndex).trim();
                }
                processedScript.append(sentence).append(String.format("<break time=\"0.3s\"/><mark name=\"%d\"/>\n", i));
            } else {
                processedScript.append(sentence).append(String.format("<break time=\"0.3s\"/><mark name=\"%d\"/>\n", i));
            }
            i++;
        }

        processedScript.append("</speak>");
        return processedScript.toString();
    }
}
