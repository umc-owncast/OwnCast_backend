package com.umc.owncast.domain.voiceexample.entity;

import com.umc.owncast.common.util.StringUtil;
import com.umc.owncast.domain.enums.VoiceCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@Table(name = "voice_example")  // 걍 voice로 할까?
@NoArgsConstructor
@AllArgsConstructor
public class VoiceExample {
    @Id
    private Long id;

    @Column(name = "voice_code")
    @Enumerated(value = EnumType.STRING)
    private VoiceCode voiceCode;

    @Column(name = "file_path")
    private String filePath;

    public boolean update(String filePath){
        if(StringUtil.isNullOrBlank(filePath)) return false;
        this.filePath = filePath;
        return true;
    }
}
