package com.umc.owncast.domain.SubCategory.entity;

import com.umc.owncast.domain.Language.entity.Language;
import com.umc.owncast.domain.MainCategory.entity.MainCategory;
import com.umc.owncast.mapping.CastCategory;
import com.umc.owncast.mapping.CastPlaylist;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "subcategory")
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