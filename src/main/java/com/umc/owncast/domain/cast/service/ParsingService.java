package com.umc.owncast.domain.cast.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParsingService {
    public String[] parseSentences(String script) {
        String[] sentences = script.split("\\.{5}|\\.{4}|\\.{3}|\\.{2}|\\.|@|\n|!|\\?");

        List<String> filteredSentences = new ArrayList<>();
        for (String sentence : sentences) {
            sentence = sentence.replace("\n", "").trim();

            if (!sentence.isEmpty()) {
                while (sentence.length() > 249) {
                    int splitIndex = sentence.lastIndexOf(" ", 249);
                    if (splitIndex == -1) {
                        splitIndex = 249;
                    }
                    filteredSentences.add(sentence.substring(0, splitIndex).trim());
                    sentence = sentence.substring(splitIndex).trim();
                }
                if (!sentence.isEmpty()) {
                    filteredSentences.add(sentence);
                }
            }
        }
        return filteredSentences.toArray(new String[0]);
    }

    public String addMarks(String[] sentences) {
        int i = 0;
        String processedScript = "<speak>";
        for (String sentence : sentences) {
            processedScript += sentence + String.format("<mark name=\"%d\"/>", i);
            i++;
        }
        processedScript += "</speak>";
        return processedScript;
    }
}
