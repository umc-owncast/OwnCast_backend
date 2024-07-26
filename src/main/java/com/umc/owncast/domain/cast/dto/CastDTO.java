package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.category.entity.MainCategory;
import lombok.*;

public class CastDTO {

    @Builder
    @Getter
    public static class CastHomeDTO {
        Long id;
        String title;
        String memberName;
        String mainCategoryName;
    }

    @Builder
    @Getter
    public static class CastPlayDTO {
        Long id;
        String title;
        String memberName;
        String mainCategoryName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CastSaveRequestDTO {
        Long castId;
        Long categoryId;
    }
}
