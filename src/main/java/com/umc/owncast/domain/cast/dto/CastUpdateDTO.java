package com.umc.owncast.domain.cast.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CastUpdateDTO {
    // 제목 커버이미지 카테고리 공개 여부
    private String title;

    private String imagePath;

    private Boolean isPublic;

    private Long playlistId;
}
