package com.umc.owncast.domain.playlist.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.castplaylist.entity.CastPlaylist;
import com.umc.owncast.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(
        name = "playlist",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id")
        }
)
@AllArgsConstructor
public class Playlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Column(name = "title", nullable = false, length = 50)
    private String name;

    @Setter
    @Column
    private String imagePath;

    @Column(nullable = false)
    private boolean isPublic;

    @Column
    private Long castSize;

    @Builder.Default
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<CastPlaylist> castPlaylistList = new ArrayList<>();

}