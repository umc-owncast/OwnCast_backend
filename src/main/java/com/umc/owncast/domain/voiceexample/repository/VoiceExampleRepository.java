package com.umc.owncast.domain.voiceexample.repository;

import com.umc.owncast.domain.voiceexample.entity.VoiceExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceExampleRepository extends JpaRepository<VoiceExample, Long> {

}
