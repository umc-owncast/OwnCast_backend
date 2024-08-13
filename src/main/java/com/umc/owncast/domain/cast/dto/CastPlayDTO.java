package com.umc.owncast.domain.cast.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CastPlayDTO {
    Long id;
    String title;
    String memberName;
    String mainCategoryName;
}
