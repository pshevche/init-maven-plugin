package io.github.pshevche.maven.init.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public interface TemplateRenderer {
    
    default String renderToString(String templateResourcePath, Map<String, Object> model) throws IOException {
        try (var out = new StringWriter()) {
            renderToWriter(templateResourcePath, model, out);
            return out.toString();
        }
    }
    
    void renderToWriter(String templateResourcePath, Map<String, Object> model, Writer out) throws IOException;
}
