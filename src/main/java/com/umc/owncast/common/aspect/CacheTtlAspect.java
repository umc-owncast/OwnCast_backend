package com.umc.owncast.common.aspect;

import com.umc.owncast.domain.member.entity.Member;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class CacheTtlAspect {

    /*private final StringRedisTemplate redisTemplate;
    private static final Duration TTL = Duration.ofMinutes(10); // TTL 10분

    public CacheTtlAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }*/

    /*@AfterReturning(
            value = "@annotation(org.springframework.transaction.annotation.Transactional) && args(member, playlistName)",
            returning = "result",
            argNames = "member,playlistName,result"
    )
    public void afterAddPlaylist(Member member, String playlistName, AddPlaylistDTO result) {
        if (result != null) {
            // 예를 들어, 새 플레이리스트 생성 후 캐시 TTL 연장
            String key = "playlist::" + result.getPlaylistId();
            redisTemplate.expire(key, TTL); // TTL 연장
            log.info("플레이리스트 '{}'의 TTL을 연장했습니다.", playlistName);
        }
    }*/
}
