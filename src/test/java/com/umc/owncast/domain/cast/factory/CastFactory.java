package com.umc.owncast.domain.cast.factory;

import com.umc.owncast.domain.cast.entity.Cast;
import com.umc.owncast.domain.enums.Formality;
import com.umc.owncast.domain.enums.Language;
import com.umc.owncast.domain.member.entity.Member;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CastFactory {

    public static Cast createCast(String title, String imagePath, String audioLength, String voice,
                                  Formality formality, Boolean isPublic, Long hits, String filePath,
                                  Member member, Language language) {
        return Cast.builder()
                .title(title)
                .imagePath(imagePath)
                .audioLength(audioLength)
                .voice(voice)
                .formality(formality)
                .isPublic(isPublic)
                .hits(hits)
                .filePath(filePath)
                .member(member)
                .language(language)
                .build();
    }

    // 여러 개의 Cast 객체를 생성하는 메서드
    public static List<Cast> createMultipleCast(int count, Member member) {
        return IntStream.range(0, count)
                .mapToObj(i -> createCast("title" + i, "imagePath" + i, "audioLength" + i, "voice" + i,
                        Formality.OFFICIAL, true, 100L, "filePath" + i, member, Language.US))
                .collect(Collectors.toList());
    }
}
