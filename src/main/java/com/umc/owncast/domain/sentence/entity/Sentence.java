package com.umc.owncast.domain.sentence.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.cast.entity.Cast;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "sentence")
@AllArgsConstructor
public class Sentence extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalSentence;

    @Column(nullable = false)
    private String translatedSentence;

    @Column(nullable = false)
    private Double timePoint;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cast_id")
    private Cast cast;

    /*@OneToOne(mappedBy = "sentence", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarkList = new ArrayList<>();*/
}