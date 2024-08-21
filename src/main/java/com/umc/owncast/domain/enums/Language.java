package com.umc.owncast.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
    US("english"),
    UK("english"),
    AUS("english"),
    IND("english"),
    JA("japanese"),
    ES("spanish"),
    ES_US("spanish");

    private final String language;

    public String getRealLanguage() {
        return this.language;
    }

}
