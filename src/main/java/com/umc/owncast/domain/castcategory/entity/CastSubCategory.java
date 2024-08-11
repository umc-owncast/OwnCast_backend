package com.umc.owncast.domain.castcategory.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.category.entity.SubCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "cast_sub_category")
@AllArgsConstructor
public class CastSubCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cast_id")
    private Cast cast;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
}