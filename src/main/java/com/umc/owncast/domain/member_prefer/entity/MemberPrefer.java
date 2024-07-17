package com.umc.owncast.domain.member_prefer.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.category.main_category.entity.MainCategory;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "maincategory")
@AllArgsConstructor
public class MemberPrefer extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private MainCategory mainCategory;
}
