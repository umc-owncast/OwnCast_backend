package com.umc.owncast.domain.cast.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CastUpdateDTO {
    private String title;

    private String imagePath;

    private Boolean isPublic;
}
