package com.umc.owncast.domain.playlist.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCastFromPlaylistDTO {
    private Long castId;
}
