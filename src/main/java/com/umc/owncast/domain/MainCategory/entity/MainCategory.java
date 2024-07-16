package com.umc.owncast.domain.MainCategory.entity;

import com.umc.owncast.domain.SubCategory.entity.SubCategory;
import com.umc.owncast.mapping.CastCategory;
import com.umc.owncast.mapping.MemberPrefer;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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