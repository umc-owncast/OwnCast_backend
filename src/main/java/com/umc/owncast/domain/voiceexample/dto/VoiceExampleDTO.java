package com.umc.owncast.domain.voiceexample.dto;

import com.umc.owncast.domain.voiceexample.entity.VoiceExample;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoiceExampleDTO {
    private String voice;
    private String filePath;

    public VoiceExampleDTO(VoiceExample voiceExample){
        voice = voiceExample.getVoiceCode().name();
        filePath = voiceExample.getFilePath();
    }
}
