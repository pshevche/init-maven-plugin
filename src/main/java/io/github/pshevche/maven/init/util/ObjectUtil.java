package io.github.pshevche.maven.init.util;

import org.jetbrains.annotations.Nullable;

public class ObjectUtil {

    private ObjectUtil() {
    }

    @Nullable
    public static <T> T firstNonNull(T... values) {
        for (T v : values) {
            if (v != null) {
                return v;
            }
        }

        return null;
    }
}
