package com.umc.owncast.domain.cast.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParsingService {
    private static final int MAX_LENGTH = 220;

    public String[] parseSentencesByDelimiter(String script){
        String[] sentences = script.split("@");
        List<String> result = new ArrayList<>();
        for(String s : sentences){
            if(s.isBlank()) continue;
            result.add(s.strip());
        }
        return result.toArray(new String[0]);
    }

    public String[] parseSentences(String script) {
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

    public String addMarks(String[] sentences) {
        int i = 1;
        StringBuilder processedScript = new StringBuilder("<speak><mark name=\"0\"/>");

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
