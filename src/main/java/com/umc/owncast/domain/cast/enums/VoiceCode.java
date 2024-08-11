package com.umc.owncast.domain.cast.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoiceCode {
    EN_US_NEURAL2_C("en-US-Neural2-C"),
    EN_US_NEWS_L("en-US-News-L"),
    EN_US_NEURAL2_E("en-US-Neural2-E"),
    EN_US_NEURAL2_D("en-US-Neural2-D"),
    EN_US_NEWS_N("en-US-News-N"),
    EN_US_POLYGLOT_1("en-US-Polyglot-1"),
    EN_US_NEURAL2_G("en-US-Neural2-G"),
    EN_US_NEURAL2_H("en-US-Neural2-H"),
    EN_US_STANDARD_F("en-US-Standard-F"),
    EN_US_STANDARD_I("en-US-Standard-I"),
    EN_US_WAVENET_B("en-US-Wavenet-B"),
    EN_US_WAVENET_J("en-US-Wavenet-J"),
    EN_GB_NEWS_H("en-GB-News-H"),
    EN_GB_NEWS_I("en-GB-News-I"),
    EN_GB_WAVENET_A("en-GB-Wavenet-A"),
    EN_GB_NEURAL2_D("en-GB-Neural2-D"),
    EN_GB_NEWS_J("en-GB-News-J"),
    EN_GB_NEWS_K("en-GB-News-K"),
    EN_GB_NEURAL2_A("en-GB-Neural2-A"),
    EN_GB_NEURAL2_F("en-GB-Neural2-F"),
    EN_GB_STANDARD_C("en-GB-Standard-C"),
    EN_GB_NEURAL2_B("en-GB-Neural2-B"),
    EN_IN_NEURAL2_D("en-IN-Neural2-D"),
    EN_IN_WAVENET_A("en-IN-Wavenet-A"),
    EN_IN_WAVENET_D("en-IN-Wavenet-D"),
    EN_IN_NEURAL2_B("en-IN-Neural2-B"),
    EN_IN_WAVENET_C("en-IN-Wavenet-C"),
    EN_IN_WAVENET_B("en-IN-Wavenet-B"),
    EN_IN_NEURAL2_A("en-IN-Neural2-A"),
    EN_IN_STANDARD_D("en-IN-Standard-D"),
    EN_IN_STANDARD_A("en-IN-Standard-A"),
    EN_IN_NEURAL2_C("en-IN-Neural2-C"),
    EN_IN_STANDARD_B("en-IN-Standard-B"),
    EN_IN_STANDARD_C("en-IN-Standard-C"),
    EN_AU_NEWS_E("en-AU-News-E"),
    EN_AU_NEWS_F("en-AU-News-F"),
    EN_AU_WAVENET_C("en-AU-Wavenet-C"),
    EN_AU_NEURAL2_B("en-AU-Neural2-B"),
    EN_AU_NEWS_G("en-AU-News-G"),
    EN_AU_POLYGLOT_1("en-AU-Polyglot-1"),
    EN_AU_STANDARD_C("en-AU-Standard-C"),
    EN_AU_WAVENET_A("en-AU-Wavenet-A"),
    EN_AU_STANDARD_D("en-AU-Standard-D"),
    EN_AU_WAVENET_B("en-AU-Wavenet-B"),
    JA_JP_NEURAL2_B("ja-JP-Neural2-B"),
    JA_JP_WAVENET_A("ja-JP-Wavenet-A"),
    JA_JP_WAVENET_B("ja-JP-Wavenet-B"),
    JA_JP_NEURAL2_D("ja-JP-Neural2-D"),
    JA_JP_WAVENET_C("ja-JP-Wavenet-C"),
    JA_JP_STANDARD_D("ja-JP-Standard-D"),
    JA_JP_STANDARD_A("ja-JP-Standard-A"),
    JA_JP_NEURAL2_C("ja-JP-Neural2-C"),
    JA_JP_WAVENET_D("ja-JP-Wavenet-D"),
    JA_JP_STANDARD_C("ja-JP-Standard-C"),
    ES_ES_NEURAL2_C("es-ES-Neural2-C"),
    ES_ES_NEURAL2_D("es-ES-Neural2-D"),
    ES_ES_NEURAL2_E("es-ES-Neural2-E"),
    ES_ES_NEURAL2_B("es-ES-Neural2-B"),
    ES_ES_NEURAL2_F("es-ES-Neural2-F"),
    ES_ES_POLYGLOT_1("es-ES-Polyglot-1"),
    ES_ES_NEURAL2_A("es-ES-Neural2-A"),
    ES_ES_STANDARD_C("es-ES-Standard-C"),
    ES_US_NEURAL2_A("es-US-Neural2-A"),
    ES_US_NEWS_F("es-US-News-F"),
    ES_US_NEWS_G("es-US-News-G"),
    ES_US_NEWS_D("es-US-News-D"),
    ES_US_NEWS_E("es-US-News-E"),
    ES_US_POLYGLOT_1("es-US-Polyglot-1"),
    ES_US_WAVENET_A("es-US-Wavenet-A"),
    ES_US_WAVENET_C("es-US-Wavenet-C");

    private final String value;

    @JsonCreator
    public static VoiceCode fromValue(String value) {
        String normalizedValue = value.toUpperCase().replace("-", "_");
        for (VoiceCode voiceCode : VoiceCode.values()) {
            if (voiceCode.name().equals(normalizedValue)) {
                return voiceCode;
            }
        }
        throw new IllegalArgumentException("Unknown voice type: " + value);
    }
}
