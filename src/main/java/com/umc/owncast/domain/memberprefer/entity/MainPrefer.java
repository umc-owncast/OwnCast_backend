package com.umc.owncast.domain.memberprefer.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.category.entity.MainCategory;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "main_prefer")
@AllArgsConstructor
public class MainPrefer extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;
}
