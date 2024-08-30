package com.umc.owncast.domain.voiceexample.repository;

import com.umc.owncast.domain.enums.VoiceCode;
import com.umc.owncast.domain.voiceexample.entity.VoiceExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoiceExampleRepository extends JpaRepository<VoiceExample, Long> {

    Optional<VoiceExample> findByVoiceCode(VoiceCode voiceCode);

    @Query(value = "SELECT * FROM voice_example WHERE UPPER(SUBSTRING(voice_code, 1, 2)) = UPPER(:languageCode) AND UPPER(SUBSTRING(voice_code, 4, 2)) = UPPER(:pronunciation)", nativeQuery = true)
    List<VoiceExample> findByLanguageCodeAndPronunciation(@Param(value="languageCode") String languageCode, @Param(value="pronunciation")  String pronunciation);
}
