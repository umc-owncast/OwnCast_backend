package com.umc.owncast.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileRequestDTO {

    @NotBlank
    String loginId;

    @NotBlank
    String username;

    @NotBlank
    String nickname;
}

