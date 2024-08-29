package com.umc.owncast.domain.category.entity;

import com.umc.owncast.domain.enums.MainCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory;

    @Column(nullable = false)
    private Boolean isUserCreated;

    /*@OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL)
    private List<CastCategory> castCategoryList = new ArrayList<>();*/
}