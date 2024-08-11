package com.umc.owncast.domain.castplaylist.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.cast.entity.Cast;
import jakarta.persistence.*;
import lombok.*;

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
