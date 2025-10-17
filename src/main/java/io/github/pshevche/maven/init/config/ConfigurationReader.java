package io.github.pshevche.maven.init.config;

import org.apache.maven.plugin.MojoExecutionException;
import org.jetbrains.annotations.Nullable;

import java.io.Console;
import java.io.File;
import java.util.Locale;

import static io.github.pshevche.maven.init.util.JavaVersionDetector.detectJavaMajorVersion;
import static io.github.pshevche.maven.init.util.ObjectUtil.firstNonNull;
import static io.github.pshevche.maven.init.util.StringUtil.isBlank;

public final class ConfigurationReader {

    private final Console console;
    private final File baseDir;

    public ConfigurationReader(File baseDir) {
        this.console = System.console();
        this.baseDir = baseDir;
    }

    public InitConfiguration read(
        String type,
        String projectName,
        String groupId,
        String packageName,
        String testFramework,
        String javaVersion) throws MojoExecutionException {
        ProjectType finalType = isBlank(type)
            ? selectProjectType()
            : ProjectType.from(type);

        String finalProjectName = readOptionValue(
            "Project name/artifactId",
            projectName,
            baseDir.getName());

        String finalGroupId = readOptionValue(
            "Project groupId",
            groupId,
            "com.example");

        String finalPackage = readOptionValue(
            "Package name",
            packageName,
            finalGroupId + "." + sanitizePackage(finalProjectName));

        TestFramework finalFramework = isBlank(testFramework)
            ? selectTestFramework()
            : TestFramework.from(testFramework);

        int finalJavaVersion = Integer.parseInt(
            readOptionValue(
                "Java version",
                javaVersion,
                detectJavaMajorVersion()));

        return new InitConfiguration(
            finalType,
            finalProjectName,
            finalGroupId,
            finalPackage,
            finalFramework,
            finalJavaVersion);
    }

    private ProjectType selectProjectType() {
        return readIndexedChoice(
            "Select type of project to generate:",
            String.format("Enter selection (default: %s) [1..2]: ", ProjectType.APPLICATION.label()),
            ProjectType.values(),
            1);
    }

    private TestFramework selectTestFramework() {
        return readIndexedChoice(
            "Select test framework:",
            String.format("Enter selection (default: %s) [1..4]: ", TestFramework.JUNIT_JUPITER.label()),
            TestFramework.values(),
            4);
    }

    private String readOptionValue(String title, @Nullable String paramValue, String defaultValue) {
        return firstNonNull(
            paramValue,
            console.readLine("%s (default: %s): ", title, defaultValue),
            defaultValue);
    }

    private <T extends FixedChoiceInitOption> T readIndexedChoice(
        String title,
        String selectionPrompt,
        T[] availableOptions,
        int defaultIndex) {
        console.printf("%s%n", title);
        for (int i = 0; i < availableOptions.length; i++) {
            console.printf("  %d: %s%n", i + 1, availableOptions[i].label());
        }
        while (true) {
            String input = console.readLine(selectionPrompt);
            String trimmed = input == null ? "" : input.trim();
            if (isBlank(trimmed)) {
                return availableOptions[defaultIndex - 1];
            }
            try {
                int idx = Integer.parseInt(trimmed);
                if (idx >= 1 && idx <= availableOptions.length) {
                    return availableOptions[idx - 1];
                }
            } catch (NumberFormatException ignored) {
                console.printf("Please enter a number between 1 and %d:%n", availableOptions.length);
            }
        }
    }

    private static String sanitizePackage(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", ".").replaceAll("^\\.+|\\.+$", "");
    }
}
