package net.oneblog.sharedconfig.prod;

import org.springframework.core.env.PropertySource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilePropertySource extends PropertySource<File> {

    public FilePropertySource() {
        super("Loader file property");
    }

    @Override
    public Object getProperty(String value) {
        Matcher m = Pattern.compile("^file\\(([^\\s]*)\\)$").matcher(value);
        if (!m.matches()) return null;
        String path = m.toMatchResult().group(1);
        try (BufferedReader br = Files.newBufferedReader(Path.of(path))) {
            return br.readLine();
        } catch (IOException e) {
            logger.trace("Failed to read file for property: " + value);
        }
        return null;
    }
}
