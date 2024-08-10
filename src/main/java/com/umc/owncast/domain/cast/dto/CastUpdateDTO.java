package com.umc.owncast.domain.cast.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CastUpdateDTO {
    private String title = "";

    private MultipartFile castImage;

    private String imagePath = "";

    private Boolean isPublic = false;
}
