package com.umc.owncast.domain.category.entity;

import com.umc.owncast.domain.category.entity.MainCategory;
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
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private MainCategory mainCategory;

    @Column(length = 50)
    private boolean isUserCreated;
    /*@OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL)
    private List<CastCategory> castCategoryList = new ArrayList<>();*/
}