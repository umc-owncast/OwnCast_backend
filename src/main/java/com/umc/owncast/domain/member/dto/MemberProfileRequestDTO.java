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
    String nickname;
    @NotBlank
    String username;
    @NotBlank
    String loginId;
    @NotBlank
    @Size(min = 8, max = 255)
    String password;
}
