package com.umc.owncast.domain.playlist.dto;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor // 레디스를 위해서 필요함
public class PlaylistResultDTO {
    String name;
    String imagePath;
    Long playlistId;
    Long totalCast;
}
