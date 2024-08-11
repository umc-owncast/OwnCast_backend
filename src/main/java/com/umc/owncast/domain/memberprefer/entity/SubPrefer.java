package com.umc.owncast.domain.memberprefer.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.category.entity.SubCategory;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "sub_prefer")
@AllArgsConstructor
public class SubPrefer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @Column(nullable = false)
    private boolean isUserCreated;
}

