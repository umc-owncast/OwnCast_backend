package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.category.entity.MainCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CastDTO {

    @Builder
    @Setter
    @Getter
    public static class CastHomeDTO {
        Long id;
        String title;
        String memberName;
        String mainCategoryName;
    }

    @Builder
    @Setter
    @Getter
    public static class CastPlayDTO {
        Long id;
        String title;
        String memberName;
        String mainCategoryName;
    }
}
