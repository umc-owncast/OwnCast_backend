package com.umc.owncast.domain.language.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    @Column
    private Long accent;

    /*@OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private List<Cast> castList = new ArrayList<>();*/
}