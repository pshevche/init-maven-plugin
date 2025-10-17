package io.github.pshevche.maven.init.util;

import org.jetbrains.annotations.Nullable;

public class StringUtil {

    private StringUtil() {
    }

    public static boolean isBlank(@Nullable String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String defaultIfBlank(@Nullable String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
    }
}
