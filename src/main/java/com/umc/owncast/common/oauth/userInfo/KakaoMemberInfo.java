package com.umc.owncast.common.oauth.userInfo;

import java.util.Map;

public class KakaoMemberInfo extends MemberInfo {
    public KakaoMemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return  String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickname() {
        return String.valueOf(attributes.get("name"));
    }
}
