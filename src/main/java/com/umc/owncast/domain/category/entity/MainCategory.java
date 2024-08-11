package com.umc.owncast.domain.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "main_category")
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