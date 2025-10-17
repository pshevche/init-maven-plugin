package io.github.pshevche.maven.init.builder;

import io.github.pshevche.maven.init.config.InitConfiguration;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;

class PomModelBuilder {

    private final InitConfiguration config;

    PomModelBuilder(InitConfiguration config) {
        this.config = config;
    }

    public Model build() {
        Model model = new Model();

        model.setModelVersion("4.0.0");
        model.setGroupId(config.getGroupId());
        model.setArtifactId(config.getProjectName());
        model.setVersion("1.0.0-SNAPSHOT");
        model.setPackaging("jar");
        model.setName(config.getProjectName());

        // Common properties instead of plugin configuration for compatibility
        model.getProperties().setProperty("project.build.sourceEncoding", "UTF-8");
        model.getProperties().setProperty("maven.compiler.release", String.valueOf(config.getJavaVersion()));

        // surefire plugin reference (inherits defaults)
        Plugin surefire = new Plugin();
        surefire.setGroupId("org.apache.maven.plugins");
        surefire.setArtifactId("maven-surefire-plugin");
        surefire.setVersion(Versions.SUREFIRE);

        Build build = new Build();
        build.addPlugin(surefire);
        model.setBuild(build);

        // Dependencies by test framework
        switch (config.getTestFramework()) {
            case JUNIT:
                model.addDependency(dep("junit", "junit", Versions.JUNIT4, "test"));
                break;
            case JUNIT_JUPITER:
                model.addDependency(dep("org.junit.jupiter", "junit-jupiter", junitJupiterVersion(config.getJavaVersion()), "test"));
                break;
            case TESTNG:
                model.addDependency(dep("org.testng", "testng", Versions.TESTNG, "test"));
                break;
            case SPOCK:
                model.addDependency(dep("org.apache.groovy", "groovy", Versions.GROOVY, "test"));
                model.addDependency(dep("org.spockframework", "spock-core", Versions.SPOCK, "test"));
                break;
        }

        return model;
    }

    private static Dependency dep(String groupId, String artifactId, String version, String scope) {
        Dependency d = new Dependency();

        d.setGroupId(groupId);
        d.setArtifactId(artifactId);
        d.setVersion(version);
        d.setScope(scope);

        return d;
    }

    private static String junitJupiterVersion(int javaVersion) {
        return javaVersion < 17 ? Versions.JUNIT5 : Versions.JUNIT6;
    }
}


