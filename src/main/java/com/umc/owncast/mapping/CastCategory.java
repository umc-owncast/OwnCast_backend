package com.umc.owncast.mapping;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.Cast.entity.Cast;
import com.umc.owncast.domain.MainCategory.entity.MainCategory;
import com.umc.owncast.domain.Member.entity.Member;
import com.umc.owncast.domain.SubCategory.entity.SubCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "maincategory")
@AllArgsConstructor
public class CastCategory extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cast_id")
    private Cast cast;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private SubCategory subCategory;
}
