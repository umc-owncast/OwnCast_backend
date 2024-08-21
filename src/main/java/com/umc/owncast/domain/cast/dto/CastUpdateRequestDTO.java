package com.umc.owncast.domain.cast.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CastUpdateRequestDTO {
    private String title;

    private MultipartFile image;

    private Boolean isPublic;

    private Long playlistId;
}
