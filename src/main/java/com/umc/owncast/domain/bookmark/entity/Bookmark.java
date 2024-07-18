package com.umc.owncast.domain.bookmark.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.sentence.entity.Sentence;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "bookmark")
@AllArgsConstructor
public class Bookmark extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cast_playlist_id")
    private CastPlaylist castPlaylist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;
}
