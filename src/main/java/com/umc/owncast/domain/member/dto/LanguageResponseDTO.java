package com.umc.owncast.domain.member.dto;

import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponseDTO {
    String language;
    Language accent;
}
