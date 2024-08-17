package com.umc.owncast.domain.playlist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class GetPlaylistDTO {
    List<CastDTO> castList;
}
