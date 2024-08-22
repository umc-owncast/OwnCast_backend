package com.umc.owncast.domain.cast.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CastUpdateRequestDTO {
    private String title;

    private Boolean isPublic;

    private Long playlistId;
}
