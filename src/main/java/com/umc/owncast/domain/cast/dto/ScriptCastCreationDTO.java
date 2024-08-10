package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.enums.Formality;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScriptCastCreationDTO {
    @NotEmpty(message = "스크립트는 필수 입력 항목입니다.")
    private String script;

    private Formality formality;

    private String voice;
}
