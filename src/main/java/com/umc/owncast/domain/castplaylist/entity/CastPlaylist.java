package com.umc.owncast.domain.castplaylist.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.playlist.entity.Playlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "cast_playlist")
@AllArgsConstructor
public class CastPlaylist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cast_id")
    private Cast cast;
}
