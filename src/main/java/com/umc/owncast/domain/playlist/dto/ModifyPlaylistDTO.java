package com.umc.owncast.domain.playlist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ModifyPlaylistDTO {
    Long playlistId;
    String playlistName;
}