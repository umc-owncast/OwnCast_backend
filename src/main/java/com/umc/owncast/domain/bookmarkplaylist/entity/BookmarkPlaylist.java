package com.umc.owncast.domain.bookmarkplaylist.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.playlist.entity.Playlist;
import com.umc.owncast.domain.sentence.entity.Sentence;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "bookmark_playlist")
@AllArgsConstructor
public class BookmarkPlaylist extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
}
