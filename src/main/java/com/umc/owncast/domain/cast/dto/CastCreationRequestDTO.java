package com.umc.owncast.domain.cast.dto;

import com.umc.owncast.domain.enums.Formality;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CastCreationRequestDTO {
    @NotEmpty(message = "키워드는 필수 입력 항목입니다.")
    private String keyword;

    private Formality formality = Formality.CASUAL;

    private String voice = "DEFAULT_VOICE";

    @Min(60)
    @Max(600)
    private int audioTime = 60;
}
