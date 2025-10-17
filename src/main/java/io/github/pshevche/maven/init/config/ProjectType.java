package io.github.pshevche.maven.init.config;

import java.util.Locale;

public enum ProjectType implements FixedChoiceInitOption {

    APPLICATION("Application"),
    LIBRARY("Library");

    private final String label;

    ProjectType(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    public static ProjectType from(String type) {
        return valueOf(type.toUpperCase(Locale.ROOT));
    }
}
