package com.umc.owncast.domain.cast.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ParsingService {
    private static final int MAX_LENGTH = 230;

    public String[] parseSentences(String script) {
        String[] sentences = script.split("\\.|@|\n|!|\\?");
        List<String> filteredSentences = Arrays.stream(sentences)
                .map(sentence -> sentence.replace("\n", "").trim())
                .filter(sentence -> !sentence.isEmpty())
                .toList();

        return filteredSentences.toArray(new String[0]);
    }

    public String addMarks(String[] sentences) {
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
                processedScript.append(sentence).append(String.format(".<mark name=\"%d\"/>\n", i));
            } else {
                processedScript.append(sentence).append(String.format(".<mark name=\"%d\"/>\n", i));
            }
            i++;
        }

        processedScript.append("</speak>");
        return processedScript.toString();
    }
}
