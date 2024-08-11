package com.umc.owncast.domain.cast.service;

import org.springframework.stereotype.Service;

@Service
public class ParsingService {
    public String[] parseSentences(String script) {
        String[] sentences = script.split("@");

        for (int i = 0; i < sentences.length; i++) {
            sentences[i] = sentences[i].replace("\n", "");
        }

        return sentences;
    }

    public String addMarks(String[] sentences) {
        int i = 0;
        String processedScript = "<speak>";
        for (String sentence : sentences) {
            processedScript += String.format("<mark name=\"%d\"/>", i) + sentence;
            i++;
        }
        processedScript += "</speak>";
        return processedScript;
    }
}
