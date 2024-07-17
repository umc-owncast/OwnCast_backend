package com.umc.owncast.domain.category.sub_category.entity;

import com.umc.owncast.domain.category.main_category.entity.MainCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "sub_category")
@AllArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private MainCategory mainCategory;

    /*@OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL)
    private List<CastCategory> castCategoryList = new ArrayList<>();*/
}