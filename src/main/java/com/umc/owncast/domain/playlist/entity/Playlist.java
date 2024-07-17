package com.umc.owncast.domain.playlist.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "playlist")
@AllArgsConstructor
public class Playlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private boolean isPublic;

    /*@OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<CastPlaylist> castPlaylistList = new ArrayList<>();*/
}