package com.umc.owncast.domain.member.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.enums.Status;
import com.umc.owncast.domain.language.entity.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, updatable = false)
    private String loginId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    @Size(min = 5)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    public void setMember(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }



    /*@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberPrefer> memberPreferList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Playlist> playlistList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Cast> castList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CastLike> castLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CastSave> castSaveList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarkList = new ArrayList<>();*/
}