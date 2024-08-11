package com.umc.owncast.domain.cast.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CastSaveDTO {
    // 제목 커버이미지 카테고리 공개 여부
    @NotEmpty
    private String title;

    @NotNull
    private Long playlistId;

    private Boolean isPublic;
}
