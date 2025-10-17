package io.github.pshevche.maven.init;

import io.github.pshevche.maven.init.builder.ProjectBuilder;
import io.github.pshevche.maven.init.config.ConfigurationReader;
import io.github.pshevche.maven.init.config.InitConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.pshevche.maven.init.util.StringUtil.isBlank;

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

        var reader = new ConfigurationReader(baseDir);
        var config = reader.read(
            type,
            projectName,
            groupId,
            packageName,
            testFramework,
            javaVersion
        );

        var builder = new ProjectBuilder(config);
        builder.initializeProject(baseDir.toPath());
    }

    private void ensureEmptyWorkingDirectory() throws MojoExecutionException {
        try {
            var dir = baseDir.toPath();
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
}
