package com.umc.owncast.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPreferRequestDTO {
    @NotBlank
    String mainCategory;
    @NotBlank
    String subCategory;
}
