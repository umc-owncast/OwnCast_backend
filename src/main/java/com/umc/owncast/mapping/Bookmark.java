package com.umc.owncast.mapping;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.Cast.entity.Cast;
import com.umc.owncast.domain.Member.entity.Member;
import com.umc.owncast.domain.Playlist.entity.Playlist;
import com.umc.owncast.domain.Sentence.entity.Sentence;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "maincategory")
@AllArgsConstructor
public class Bookmark extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;
}
