package com.umc.owncast.domain.cast.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.cast.dto.CastUpdateDTO;
import com.umc.owncast.domain.enums.Formality;
import com.umc.owncast.domain.language.entity.Language;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer audioLength;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    public void update(CastUpdateDTO updateRequest) {
        if(Objects.nonNull(updateRequest.getTitle()) && !updateRequest.getTitle().isBlank())
            this.title = updateRequest.getTitle();
        if(Objects.nonNull(updateRequest.getIsPublic()))
            this.isPublic = updateRequest.getIsPublic();
        if(Objects.nonNull(updateRequest.getImagePath()) && !updateRequest.getImagePath().isBlank())
            this.imagePath = updateRequest.getImagePath();
    }

    public void updateHits(){
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

    @OneToMany(mappedBy = "cast", cascade = CascadeType.ALL)
    private List<Sentence> sentenceList = new ArrayList<>();*/
}
