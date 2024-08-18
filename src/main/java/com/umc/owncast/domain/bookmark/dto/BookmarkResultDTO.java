package com.umc.owncast.domain.bookmark.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookmarkResultDTO {
    Long castId;
    String originalSentence;
    String translatedSentence;
}