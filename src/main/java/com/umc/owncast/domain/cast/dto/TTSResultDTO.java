package com.umc.owncast.domain.cast.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TTSResultDTO {
    List<Double> timePointList;
    String mp3Path;
}
