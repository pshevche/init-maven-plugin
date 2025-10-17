package io.github.pshevche.maven.init.config;

import java.util.Locale;

public enum TestFramework implements FixedChoiceInitOption {

    JUNIT("JUnit 4"),
    TESTNG("TestNG"),
    SPOCK("Spock"),
    JUNIT_JUPITER("JUnit Jupiter");

    private final String label;

    TestFramework(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    public static TestFramework from(String testFramework) {
        return valueOf(testFramework.toUpperCase(Locale.ROOT));
    }
}
