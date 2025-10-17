package io.github.pshevche.maven.init.render;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemWriter {

    public void writeString(Path target, String content) throws IOException {
        createParentDirectories(target);
        try (Writer w = new OutputStreamWriter(Files.newOutputStream(target), StandardCharsets.UTF_8)) {
            w.write(content);
        }
    }

    public Writer newWriter(Path target) throws IOException {
        createParentDirectories(target);
        return new OutputStreamWriter(Files.newOutputStream(target), StandardCharsets.UTF_8);
    }

    private static void createParentDirectories(Path target) throws IOException {
        Path parent = target.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}


