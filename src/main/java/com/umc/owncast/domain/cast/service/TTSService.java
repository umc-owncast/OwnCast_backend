package com.umc.owncast.domain.cast.service;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class TTSService {

    public void createSpeech(String script) throws Exception {
        //languagecode,name,gender,(unique할 수 있는 파일 이름용 정보 논의 필요) 넘어오면 됨
        // + 추가로 S3 설정 후 파일 저장 로직 구현 재설정
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(script).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                                                        .setLanguageCode("en-US")
                                                        .setName("en-US-Journey-D")
                                                        .setSsmlGender(SsmlVoiceGender.MALE)
                                                        .build();
            AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            String title = "set_later";
            String outputFilePath = "src/main/resources/speech/output_" + title + ".mp3";
            try (OutputStream out = new FileOutputStream(outputFilePath)) {
                out.write(audioContents.toByteArray());
            }
        }
    }
}
