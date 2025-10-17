package io.github.pshevche.maven.init.builder;

import io.github.pshevche.maven.init.config.InitConfiguration;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectBuilder {

    private final InitConfiguration config;

    public ProjectBuilder(InitConfiguration config) {
        this.config = config;
    }

    public void initializeProject(Path baseDir) throws MojoExecutionException {
        writePom(baseDir);
        writeSources(baseDir);
        writeTestSources(baseDir);
    }

    private void writePom(Path baseDir) throws MojoExecutionException {
        var pomPath = baseDir.resolve("pom.xml");
        var pom = new PomModelBuilder(config).build();

        try (var out = Files.newOutputStream(pomPath)) {
            new MavenXpp3Writer().write(out, pom);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write the generated POM", e);
        }
    }

    private void writeSources(Path targetPath) {

    }

    private void writeTestSources(Path targetPath) {

    }
}
