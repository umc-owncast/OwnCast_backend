package com.umc.owncast.domain.member.factory;

import com.umc.owncast.domain.member.entity.Member;
import com.umc.owncast.domain.enums.Status;
import com.umc.owncast.domain.enums.Language;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemberFactory {

    public static Member createMember(String loginId, String username, String password, String nickname) {
        return Member.builder()
                .loginId(loginId)
                .username(username)
                .password(password)
                .nickname(nickname)
                .status(Status.ACTIVE)
                .language(Language.US)
                .build();
    }

    public static List<Member> createMultipleMembers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createMember("loginId" + i, "username" + i, "password" + i, "nickname" + i))
                .collect(Collectors.toList());
    }
}
