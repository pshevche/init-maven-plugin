package io.github.pshevche.maven.init.options;

public enum ProjectType implements InitOption {

    APPLICATION("Application"),
    LIBRARY("Library");

    private final String description;

    ProjectType(String description) {
        this.description = description;
    }

    @Override
    public String label() {
        return description;
    }
}
