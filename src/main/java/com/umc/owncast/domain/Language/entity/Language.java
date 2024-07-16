package com.umc.owncast.domain.Language.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.Cast.entity.Cast;
import com.umc.owncast.domain.Member.entity.Member;
import com.umc.owncast.domain.Sentence.entity.Sentence;
import com.umc.owncast.mapping.CastCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "language")
@AllArgsConstructor
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String languageName;

    /*@OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private List<Cast> castList = new ArrayList<>();*/
}