package com.umc.owncast.domain.member.entity;


import com.umc.owncast.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@Entity
@Table(name = "member")
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    /* 예시 엔티티 -> 나중에 이름 바꿔도 됩니다 */
    @Id
    private Long id;
}
