package com.umc.owncast.domain.Playlist.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.Member.entity.Member;
import com.umc.owncast.mapping.CastPlaylist;
import com.umc.owncast.mapping.MemberPrefer;
import jakarta.persistence.*;
import lombok.*;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    @Column
    private String imagePath;

    @Column(nullable = false)
    private boolean isPublic;

    /*@OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<CastPlaylist> castPlaylistList = new ArrayList<>();*/
}