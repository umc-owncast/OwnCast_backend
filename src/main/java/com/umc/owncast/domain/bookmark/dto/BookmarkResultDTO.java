package com.umc.owncast.domain.bookmark.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookmarkResultDTO {
    Long castId;
    Long sentenceId;
    String castURL;
    String originalSentence;
    String translatedSentence;
    Double start;
    Double end;
}
