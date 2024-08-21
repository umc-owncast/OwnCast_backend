package com.umc.owncast.common.util;

import java.util.Objects;

public class StringUtil {
    public static boolean isNullOrBlank(String s) {
        return Objects.isNull(s) || s.isBlank();
    }
}
