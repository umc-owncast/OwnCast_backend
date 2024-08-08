package com.umc.owncast.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class MemberRequest {

    @Getter
    public static class joinLoginIdDto {

        @NotBlank
        String nickname;

        @NotBlank
        String loginId;

        @NotBlank
        String password;
    }

    @Getter
    public static class loginDto {
        String loginId;
        String password;
    }


}
