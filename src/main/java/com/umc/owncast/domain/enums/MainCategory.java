package com.umc.owncast.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MainCategory {
    SPORTS("스포츠"),
    MUSIC("음악"),
    BOOK("책"),
    ART("미술"),
    DRAMA("드라마/영화"),
    FOOD("음식"),
    NEWS("시사/뉴스"),
    ETC("직접 입력");

    private final String subCategory;

    public String getKrSubCategory() {
        return this.subCategory;
    }
}
