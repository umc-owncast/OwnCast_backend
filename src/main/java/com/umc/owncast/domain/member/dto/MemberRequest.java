package com.umc.owncast.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class MemberRequest {

    @Getter
    public static class joinLoginIdDto {

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

    @Getter
    public static class loginDto {
        String loginId;
        String password;
    }

    @Getter
    public static class memberPreferDto {
        @NotBlank
        Long mainCategoryId;
        Long subCategoryId;
        String etc;
    }

}
