package io.github.pshevche.maven.init.config;

public final class InitConfiguration {

    private final String projectName;
    private final String groupId;
    private final String packageName;
    private final TestFramework testFramework;
    private final int javaVersion;

    public InitConfiguration(
        String projectName,
        String groupId,
        String packageName,
        TestFramework testFramework,
        int javaVersion
    ) {
        this.projectName = projectName;
        this.groupId = groupId;
        this.packageName = packageName;
        this.testFramework = testFramework;
        this.javaVersion = javaVersion;
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
