package io.github.pshevche.maven.init.render;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface TemplateRenderer {
    String renderToString(String templateResourcePath, Map<String, Object> model) throws IOException;
    void renderToWriter(String templateResourcePath, Map<String, Object> model, Writer out) throws IOException;
}


