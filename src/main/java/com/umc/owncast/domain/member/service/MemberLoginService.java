package com.umc.owncast.domain.member.service;

import com.umc.owncast.common.jwt.JwtTokenDTO;

public interface MemberLoginService {
    JwtTokenDTO login(String id, String password);
}
