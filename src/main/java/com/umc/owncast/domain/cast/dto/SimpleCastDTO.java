package com.umc.owncast.domain.cast.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleCastDTO {
    private Long id;
    private String title;
    private String imagePath;
    private Integer audioLength;
    private Boolean isPublic;
    private Long hits;
}
