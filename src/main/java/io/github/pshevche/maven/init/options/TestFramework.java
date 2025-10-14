package io.github.pshevche.maven.init.options;

public enum TestFramework implements InitOption {

    JUNIT("JUnit 4"),
    TESTNG("TestNG"),
    SPOCK("Spock"),
    JUNIT_JUPITER("JUnit Jupiter");

    private final String description;

    TestFramework(String description) {
        this.description = description;
    }

    @Override
    public String label() {
        return description;
    }
}
