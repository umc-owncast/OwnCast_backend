package com.umc.owncast.domain.bookmark.dto;


import lombok.Builder;
import lombok.Getter;

public class BookMarkDTO {

    @Builder
    @Getter
    public static class BookMarkResultDTO {
        Long castId;
        String originalSentence;
        String translatedSentence;
    }

    @Builder
    @Getter
    public static class BookMarkSaveResultDTO {
        Long bookmarkId;
    }
}
