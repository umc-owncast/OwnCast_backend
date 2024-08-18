package com.umc.owncast.domain.cast.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.common.util.StringUtil;
import com.umc.owncast.domain.cast.dto.CastUpdateDTO;
import com.umc.owncast.domain.enums.Formality;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.sentence.entity.Sentence;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "cast")
@AllArgsConstructor
public class Cast extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 50)
    private String title;

    @Column
    private String imagePath;

    @Column(nullable = false)
    private String audioLength;

    @Column
    private String voice;

    @Enumerated(EnumType.STRING)
    private Formality formality;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(name = "hits")
    private Long hits;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Builder.Default
    @OneToMany(mappedBy = "cast", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sentence> sentences = new ArrayList<>();

    public void update(CastUpdateDTO updateRequest) {
        if (!StringUtil.isBlank(updateRequest.getTitle()))
            this.title = updateRequest.getTitle();
        if (Objects.nonNull(updateRequest.getIsPublic()))
            this.isPublic = updateRequest.getIsPublic();
        if (!StringUtil.isBlank(updateRequest.getImagePath()))
            this.imagePath = updateRequest.getImagePath();
    }

    public void updateHits() {
        this.hits++;
    }

    /*@OneToMany(mappedBy = "cast", cascade = CascadeType.ALL)
    private List<CastCategory> castCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "cast", cascade = CascadeType.ALL)
    private List<CastPlaylist> castPlaylistList = new ArrayList<>();

    @OneToMany(mappedBy = "cast", cascade = CascadeType.ALL)
    private List<CastSave> castSaveList = new ArrayList<>();

    @OneToMany(mappedBy = "cast", cascade = CascadeType.ALL)
    private List<CastLike> castLikeList = new ArrayList<>();
    */
}
