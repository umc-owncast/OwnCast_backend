package com.umc.owncast.domain.member.entity;

import com.umc.owncast.common.entity.BaseTimeEntity;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.enums.Status;
import jakarta.persistence.*;
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

    public void updateMember(String loginId, String username, String nickname){
        this.loginId = loginId;
        this.username = username;
        this.nickname = nickname;
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