package com.umc.owncast.domain.member.dto;

import com.umc.owncast.common.oauth.userInfo.KakaoMemberInfo;
import com.umc.owncast.common.oauth.userInfo.MemberInfo;
import com.umc.owncast.domain.enums.SocialType;
import com.umc.owncast.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthRequestDTO {
    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값
    private MemberInfo userInfo; // 소셜 타입별 로그인 유저 정보


    @Builder
    public OAuthRequestDTO(String nameAttributeKey, MemberInfo userInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.userInfo = userInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthRequestDTO of(SocialType socialType, String usernameAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(usernameAttributeName, attributes);
        }
        return null; // 추후 소셜로그인 추가된다면 추가
    }

    private static OAuthRequestDTO ofKakao(String usernameAttributeName, Map<String, Object> attributes) {
        return OAuthRequestDTO.builder()
                .nameAttributeKey(usernameAttributeName)
                .userInfo(new KakaoMemberInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * role은 GUEST로 설정
     */
    public Member toEntity(SocialType socialType, MemberInfo userInfo) {
        return Member.builder()
                .socialType(socialType)
                .socialId(userInfo.getId())
                .loginId(userInfo.getId())
                .username(userInfo.getNickname())
                .build();
    }

}
