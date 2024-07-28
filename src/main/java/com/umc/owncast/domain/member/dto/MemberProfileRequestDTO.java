package com.umc.owncast.domain.member.dto;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class MemberProfileRequestDTO {

    @Pattern(regexp = "^[A-Za-z0-9가-힣-_]{1,10}$", message = "아이디 형식에 맞지 않습니다.")
    private String userId;

    @Pattern(regexp = "^[A-Za-z0-9가-힣-_]{1,10}$", message = "닉네임 형식에 맞지 않습니다.")
    private String nickname;

    @Pattern(regexp = "^[A-Za-z0-9가-힣-_]{1,5}$", message = "닉네임 형식에 맞지 않습니다.")
    private String name;
}

