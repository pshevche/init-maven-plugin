package io.github.pshevche.maven.init.util;

import org.apache.maven.plugin.MojoExecutionException;

import static io.github.pshevche.maven.init.util.StringUtil.isBlank;

public class JavaVersionDetector {

    private JavaVersionDetector() {
    }

    public static String detectJavaMajorVersion() throws MojoExecutionException {
        String v = System.getProperty("java.version");
        if (isBlank(v)) {
            throw new MojoExecutionException("Failed to detect the Java version from 'java.version' system property");
        }

        // Examples: "11.0.23", "17", "1.8.0_392"
        if (v.startsWith("1.")) {
            // Legacy format 1.x -> x
            String[] parts = v.split("\\.");
            return parts[1];
        }

        int dot = v.indexOf('.');
        return dot > 0 ? v.substring(0, dot) : v;
    }
}

