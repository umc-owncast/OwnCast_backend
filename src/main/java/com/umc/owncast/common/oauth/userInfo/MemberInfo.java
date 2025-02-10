package com.umc.owncast.common.oauth.userInfo;

import java.util.Map;

public abstract class MemberInfo {
    protected Map<String, Object> attributes;

    public MemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getNickname();
    public abstract String getId(); // 소셜 식별 값: 카카오 - "id"
}
