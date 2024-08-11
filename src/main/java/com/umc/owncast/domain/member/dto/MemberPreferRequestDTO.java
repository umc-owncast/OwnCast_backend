package com.umc.owncast.domain.member.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPreferRequestDTO {
    long mainCategoryId;
    long subCategoryId;
    String etc;
}
