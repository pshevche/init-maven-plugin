package io.github.pshevche.maven.init.config;

import org.apache.maven.plugin.MojoExecutionException;
import org.jetbrains.annotations.Nullable;

import java.io.Console;
import java.io.File;
import java.util.Locale;

import static io.github.pshevche.maven.init.util.JavaVersionDetector.detectJavaMajorVersion;
import static io.github.pshevche.maven.init.util.ObjectUtil.firstNonNull;
import static io.github.pshevche.maven.init.util.StringUtil.defaultIfBlank;
import static io.github.pshevche.maven.init.util.StringUtil.isBlank;

public final class ConfigurationReader {

    private final Console console;
    private final File baseDir;

    public ConfigurationReader(File baseDir) {
        this.console = System.console();
        this.baseDir = baseDir;
    }

    public InitConfiguration read(
        String projectName,
        String groupId,
        String packageName,
        String testFramework,
        String javaVersion
    ) throws MojoExecutionException {
        var finalProjectName = readOptionValue(
            "Project name/artifactId",
            projectName,
            baseDir.getName());

        var finalGroupId = readOptionValue(
            "Project groupId",
            groupId,
            "com.example");

        var finalPackage = readOptionValue(
            "Package name",
            packageName,
            finalGroupId + "." + sanitizePackage(finalProjectName));

        var finalFramework = isBlank(testFramework)
            ? selectTestFramework()
            : TestFramework.from(testFramework);

        var finalJavaVersion = Integer.parseInt(
            readOptionValue(
                "Java version",
                javaVersion,
                detectJavaMajorVersion()));

        return new InitConfiguration(
            finalProjectName,
            finalGroupId,
            finalPackage,
            finalFramework,
            finalJavaVersion);
    }

    private TestFramework selectTestFramework() {
        return readFixedChoice(
            "Select test framework:",
            String.format("Enter selection (default: %s) [1..4]: ", TestFramework.JUNIT_JUPITER.label()),
            TestFramework.values(),
            4);
    }

    private String readOptionValue(String title, @Nullable String paramValue, String defaultValue) {
        return firstNonNull(
            paramValue,
            defaultIfBlank(console.readLine("%s (default: %s): ", title, defaultValue), defaultValue)
        );
    }

    private <T extends FixedChoiceInitOption> T readFixedChoice(
        String title,
        String selectionPrompt,
        T[] availableOptions,
        int defaultIndex
    ) {
        console.printf("%s%n", title);
        for (var i = 0; i < availableOptions.length; i++) {
            console.printf("  %d: %s%n", i + 1, availableOptions[i].label());
        }
        while (true) {
            var input = console.readLine(selectionPrompt);
            var trimmed = input == null ? "" : input.trim();
            if (isBlank(trimmed)) {
                return availableOptions[defaultIndex - 1];
            }
            try {
                var idx = Integer.parseInt(trimmed);
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
