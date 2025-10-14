package io.github.pshevche.maven.init;

import io.github.pshevche.maven.init.options.InitOption;
import io.github.pshevche.maven.init.options.ProjectType;
import io.github.pshevche.maven.init.options.TestFramework;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jetbrains.annotations.Nullable;

import java.io.Console;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static io.github.pshevche.maven.init.internal.JavaVersionDetector.detectJavaMajorVersion;
import static io.github.pshevche.maven.init.internal.StringUtil.defaultIfBlank;
import static io.github.pshevche.maven.init.internal.StringUtil.isBlank;
import static java.util.Objects.requireNonNull;

@Mojo(
    name = "init",
    defaultPhase = LifecyclePhase.NONE,
    requiresProject = false,
    threadSafe = true
)
public class InitMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.basedir}", readonly = true, required = true)
    private File baseDir;

    @Nullable
    @Parameter(property = "type")
    private String type;

    @Nullable
    @Parameter(property = "groupId")
    private String groupId;

    @Nullable
    @Parameter(property = "javaVersion")
    private String javaVersion;

    @Nullable
    @Parameter(property = "package")
    private String packageName;

    @Nullable
    @Parameter(property = "projectName")
    private String projectName;

    @Nullable
    @Parameter(property = "testFramework")
    private String testFramework;

    @Override
    public void execute() throws MojoExecutionException {
        ensureEmptyWorkingDirectory();
        failIfMissingParametersAndNonInteractive();

        promptForMissingParameterValues();
        logSpecifiedConfiguration();

        // Generation will be implemented in subsequent steps (templates, pom writing,
        // etc.)
    }

    private void ensureEmptyWorkingDirectory() throws MojoExecutionException {
        try {
            Path dir = baseDir.toPath();
            if (Files.exists(dir) && Files.list(dir).findFirst().isPresent()) {
                throw new MojoExecutionException("Target directory is not empty: " + baseDir.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to inspect target directory: " + baseDir, e);
        }
    }

    private void failIfMissingParametersAndNonInteractive() throws MojoExecutionException {
        if (hasMissingParameters() && !isInteractiveSession()) {
            throw new MojoExecutionException(
                "Required parameters are missing and interactive input is not available. Provide -Dtype, -Dname, -DgroupId, -DpackageName, -DtestFramework, -DjavaVersion"
            );
        }
    }

    private boolean hasMissingParameters() {
        return isBlank(type)
            || isBlank(projectName)
            || isBlank(groupId)
            || isBlank(packageName)
            || isBlank(testFramework)
            || isBlank(javaVersion);
    }

    private boolean isInteractiveSession() {
        return System.console() != null;
    }

    private void promptForMissingParameterValues() throws MojoExecutionException {
        Console console = System.console();

        if (isBlank(type)) {
            type = readIndexedChoice(
                console,
                "Select type of project to generate:",
                ProjectType.values(),
                1,
                String.format("Enter selection (default: %s) [1..2]: ", ProjectType.APPLICATION.label())
            ).label();
        }
        if (isBlank(projectName)) {
            String defaultProjectName = baseDir.getName();
            projectName = defaultIfBlank(
                console.readLine("Project name/artifactId (default: %s): ", defaultProjectName),
                defaultProjectName
            );
        }
        if (isBlank(groupId)) {
            groupId = defaultIfBlank(
                console.readLine("Project groupId (default: %s): ", "com.example"),
                "com.example"
            );
        }
        if (isBlank(packageName)) {
            String defaultPackage = groupId + "." + sanitizePackage(requireNonNull(projectName));
            packageName = defaultIfBlank(
                console.readLine("Package name (default: %s): ", defaultPackage),
                defaultPackage
            );
        }
        if (isBlank(testFramework)) {
            testFramework = readIndexedChoice(
                console,
                "Select test framework:",
                TestFramework.values(),
                4,
                String.format("Enter selection (default: %s) [1..4]: ", TestFramework.JUNIT_JUPITER.label())
            ).label();
        }
        if (isBlank(javaVersion)) {
            String defaultJavaVersion = detectJavaMajorVersion();
            javaVersion = defaultIfBlank(
                console.readLine("Java version (default: %s): ", defaultJavaVersion),
                defaultJavaVersion
            );
        }
    }

    private static String sanitizePackage(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", ".").replaceAll("^\\.+|\\.+$", "");
    }

    private static InitOption readIndexedChoice(
        Console console,
        String title,
        InitOption[] options,
        int defaultIndex,
        String selectionPrompt
    ) {
        console.printf("%s%n", title);
        for (int i = 0; i < options.length; i++) {
            console.printf("  %d: %s%n", i + 1, options[i].label());
        }

        while (true) {
            String input = console.readLine(selectionPrompt);
            String trimmed = input == null ? "" : input.trim();
            if (isBlank(trimmed)) {
                return options[defaultIndex - 1];
            }

            try {
                int idx = Integer.parseInt(trimmed);
                if (idx >= 1 && idx <= options.length) {
                    return options[idx - 1];
                }
            } catch (NumberFormatException ignored) {
                console.printf("Please enter a number between 1 and %d:", options.length);
            }
        }
    }

    private void logSpecifiedConfiguration() {
        getLog().info("Initializing Maven project with options:");
        getLog().info("  type=" + type);
        getLog().info("  name=" + projectName);
        getLog().info("  groupId=" + groupId);
        getLog().info("  packageName=" + packageName);
        getLog().info("  testFramework=" + testFramework);
        getLog().info("  javaVersion=" + javaVersion);
    }
}
