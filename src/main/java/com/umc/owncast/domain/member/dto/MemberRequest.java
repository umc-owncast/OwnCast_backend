package com.umc.owncast.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class MemberRequest {

    @Getter
    public static class joinLoginIdDto {

        @NotBlank
        @Size(max = 10, message = "닉네임은 10자 이내여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9-_가-힣]*$", message = "닉네임은 영문, 한글, 숫자, '-', '_'만 포함할 수 있습니다.")
        String nickname;

        @NotBlank
        @Size(max = 5, message = "이름은 5자 이내여야 합니다.")
        String username;

        @NotBlank
        @Size(min = 5, max = 15, message = "아이디는 5자 이상 15자 이내여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문과 숫자만 포함할 수 있습니다.")
        String loginId;

        @NotBlank
        @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
        String password;

        @NotBlank
        String language;

        @NotBlank
        String mainCategory;

        @NotBlank
        String subCategory;
    }

    @Getter
    public static class loginDto {
        String loginId;
        String password;
    }

    @Getter
    public static class memberPreferDto {
        @NotBlank
        String language;
        @NotBlank
        Long mainCategoryId;
        Long subCategoryId;
        String etc;
    }
}
