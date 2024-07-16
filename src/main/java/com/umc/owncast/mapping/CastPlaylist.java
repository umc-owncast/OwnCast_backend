package com.umc.owncast.mapping;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.Cast.entity.Cast;
import com.umc.owncast.domain.Playlist.entity.Playlist;
import com.umc.owncast.domain.SubCategory.entity.SubCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "maincategory")
@AllArgsConstructor
public class CastPlaylist extends BaseTimeEntity{
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
