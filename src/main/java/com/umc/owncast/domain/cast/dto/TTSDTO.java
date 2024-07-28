package com.umc.owncast.domain.cast.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TTSDTO {
    @NotEmpty(message = "voice type is null")
    private String voice;

    @NotEmpty(message = "language type is null")
    private String language;

    @NotEmpty(message = "script is null")
    private String script;
}
