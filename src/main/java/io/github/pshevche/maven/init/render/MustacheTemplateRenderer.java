package io.github.pshevche.maven.init.render;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MustacheTemplateRenderer implements TemplateRenderer {

    private final MustacheFactory mustacheFactory;

    public MustacheTemplateRenderer() {
        this.mustacheFactory = new DefaultMustacheFactory();
    }

    @Override
    public String renderToString(String templateResourcePath, Map<String, Object> model) throws IOException {
        try (StringWriter out = new StringWriter()) {
            renderToWriter(templateResourcePath, model, out);
            return out.toString();
        }
    }

    @Override
    public void renderToWriter(String templateResourcePath, Map<String, Object> model, Writer out) throws IOException {
        try (InputStream in = getResourceAsStream(templateResourcePath);
             InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            Mustache mustache = mustacheFactory.compile(reader, templateResourcePath);
            mustache.execute(out, model).flush();
        }
    }

    private InputStream getResourceAsStream(String resourcePath) throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IOException("Template resource not found on classpath: " + resourcePath);
        }
        return in;
    }
}


