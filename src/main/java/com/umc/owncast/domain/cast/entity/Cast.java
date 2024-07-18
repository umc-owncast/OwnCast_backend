package com.umc.owncast.domain.cast.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.enums.Voice;
import com.umc.owncast.domain.language.entity.Language;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "cast_table")
@AllArgsConstructor
public class Cast extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column
    private String imagePath;

    @Column(nullable = false)
    private Integer audioLength;

    @Enumerated(EnumType.STRING)
    private Voice voice;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(name = "hits")
    private Long hits;

    @Column(name = "file_path")
    private Long filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

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
