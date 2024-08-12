package com.umc.owncast.domain.cast.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OtherCastRequestDTO {
    Long castId;
    Long playlistId;
}
