package com.umc.owncast.domain.member.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.enums.SocialType;
import com.umc.owncast.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Column(nullable = false, length = 30)
    private String loginId;

    @Column(nullable = false, length = 50)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Setter
    @Enumerated(EnumType.STRING)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType; // 일반로그인인 경우, null

    @Column(name = "social_id")
    private String socialId;

    public void updateMember(String loginId, String username, String nickname){
        this.loginId = loginId;
        this.username = username;
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object m) {
        if(!(m instanceof Member)) return false;
        return Objects.equals(((Member) m).getId(), getId());
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