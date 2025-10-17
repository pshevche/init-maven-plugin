package io.github.pshevche.maven.init.config;

public final class InitConfiguration {

    private final ProjectType projectType;
    private final String projectName;
    private final String groupId;
    private final String packageName;
    private final TestFramework testFramework;
    private final int javaVersion;

    public InitConfiguration(
        ProjectType projectType,
        String projectName,
        String groupId,
        String packageName,
        TestFramework testFramework,
        int javaVersion
    ) {
        this.projectType = projectType;
        this.projectName = projectName;
        this.groupId = groupId;
        this.packageName = packageName;
        this.testFramework = testFramework;
        this.javaVersion = javaVersion;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getPackageName() {
        return packageName;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public int getJavaVersion() {
        return javaVersion;
    }
}
