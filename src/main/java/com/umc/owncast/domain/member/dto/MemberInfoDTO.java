package com.umc.owncast.domain.member.dto;

import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class MemberInfoDTO {
    String loginId;
    String username;
    String nickname;
    Language language;
    String mainCategory;
    String subCategory;
}
