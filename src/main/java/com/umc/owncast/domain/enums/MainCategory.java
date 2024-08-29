package com.umc.owncast.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    /** String에서 MainCategory 추출 */
    public static MainCategory fromValue(String value) {
        String normalizedValue = value.toUpperCase().strip();

        switch(normalizedValue){
            case "스포츠":
                return SPORTS;
            case "음악":
                return MUSIC;
            case "책":
                return BOOK;
            case "미술":
                return ART;
            case "드라마":
            case "영화":
            case "드라마/영화":
            case "영화/드라마":
                return DRAMA;
            case "음식":
                return FOOD;
            case "시사":
            case "뉴스":
            case "시사/뉴스":
            case "뉴스/시사":
                return NEWS;
            case "직접 입력":
                return ETC;
            default:
                break;
        }

        for (MainCategory category : MainCategory.values()) {
            if (category.name().equals(normalizedValue)) {
                return category;
            }
        }

        throw new IllegalArgumentException("Unknown main category: " + value);
    }
}
