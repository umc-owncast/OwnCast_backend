package com.umc.owncast.domain.Cast.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.Language.entity.Language;
import com.umc.owncast.domain.Member.entity.Member;
import com.umc.owncast.domain.Sentence.entity.Sentence;
import com.umc.owncast.enums.Voice;
import com.umc.owncast.mapping.CastCategory;
import com.umc.owncast.mapping.CastLike;
import com.umc.owncast.mapping.CastPlaylist;
import com.umc.owncast.mapping.CastSave;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
