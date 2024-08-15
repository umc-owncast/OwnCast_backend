package com.umc.owncast.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberPasswordRequestDTO {
    @NotBlank
    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이내여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    String password;
}
