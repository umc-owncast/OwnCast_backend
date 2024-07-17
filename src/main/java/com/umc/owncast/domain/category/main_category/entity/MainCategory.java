package com.umc.owncast.domain.category.main_category.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "maincategory")
@AllArgsConstructor
public class MainCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    /*@OneToMany(mappedBy = "mainCategory", cascade = CascadeType.ALL)
    private List<SubCategory> subCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "mainCategory", cascade = CascadeType.ALL)
    private List<MemberPrefer> memberPreferList = new ArrayList<>();*/
}