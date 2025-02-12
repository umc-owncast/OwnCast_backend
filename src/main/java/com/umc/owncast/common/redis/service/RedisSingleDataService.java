package com.umc.owncast.common.redis.service;

import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public interface RedisSingleDataService {

    int setSingleData(String key, Object value);                        // Redis 단일 데이터 값을 등록/수정합니다.

    int setSingleData(String key, Object value, Duration duration);     // Redis 단일 데이터 값을 등록/수정합니다.(duration 값이 존재하면 메모리 상 유효시간을 지정합니다.)

    String getSingleData(String key);                                   // Redis 키를 기반으로 단일 데이터의 값을 조회합니다.

    int deleteSingleData(String key);                                   // Redis 키를 기반으로 단일 데이터의 값을 삭제합니다.
}
