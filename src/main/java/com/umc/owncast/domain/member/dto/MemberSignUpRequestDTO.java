package com.umc.owncast.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
public class MemberSignUpRequestDTO {

    @NotBlank(message = "5_15자의 영문 대/소문자, 숫자만 사용해 주세요")
    private String id;

    @Pattern(regexp = "^[a-zA-z0-9]{6,20}$", message = "에러메시지")
    private String password;

    @NotBlank(message = "10자 이내로 입력해주세요")
    private String nickname;

    @NotBlank(message = "한글 기준 5자 이내로 입력해주세요")
    private String name;
}
