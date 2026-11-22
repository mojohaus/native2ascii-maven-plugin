/*
 * The MIT License
 * Copyright (c) 2014-2022 MojoHaus
 * Copyright (c) 2007 The Codehaus
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.codehaus.mojo.native2ascii.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author David Matějček
 */
class Native2AsciiMojoTest {

    @Test
    void file() throws Exception {
        final Native2AsciiMojo mojo = new Native2AsciiMojo();
        mojo.encoding = "UTF-8";
        mojo.srcDir = getSourceDirectory();
        mojo.targetDir = mojo.srcDir.getParentFile();
        mojo.includes = new String[] {"xxx.properties"};
        mojo.excludes = new String[0];
        mojo.execute();

        final Properties properties = loadFile(new File(mojo.targetDir, "xxx.properties"));
        assertEquals("~", properties.get("1"));
        assertEquals("", properties.get("2"));
        assertNull(properties.get("3"));
        assertEquals("粗 Řízeček utíká a řeže zatáčky! §", properties.get("4"));
        assertNull(properties.get("5"));
        assertEquals("more tests needed!", properties.get("6"));
        assertEquals("", properties.get("7"));
    }

    @Test
    void subdirectories() throws Exception {
        final Native2AsciiMojo mojo = new Native2AsciiMojo();
        mojo.encoding = "UTF-8";
        mojo.srcDir = getSourceDirectory();
        mojo.targetDir = mojo.srcDir.getParentFile();
        mojo.includes = new String[] {"subdirs/**/*"};
        mojo.excludes = new String[0];
        mojo.execute();

        final File file = new File(mojo.targetDir.getCanonicalPath() + File.separator + "subdirs/x2/x3/sub.properties");
        assertTrue(file.exists(), "file does not exist: " + file);
        final Properties properties = loadFile(file);
        assertEquals("šílené!", properties.get("crazy"));
    }

    @Test
    void convertsIntoMissingTargetDirectoryAndLogsEachFile(@TempDir Path tempDir) throws Exception {
        // the mojo logs canonical paths, which differ from the temp dir on macOS
        Path temp = tempDir.toRealPath();
        Path src = writeProperties(temp.resolve("mycustomdir/resource_ru.properties"));
        RecordingLog log = new RecordingLog();

        Native2AsciiMojo mojo = newMojo(temp.resolve("mycustomdir"), temp.resolve("classes/i18n"), log);
        mojo.execute();

        Path target = temp.resolve("classes/i18n/resource_ru.properties");
        assertEquals(
                "hello=\\u041F\\u0440\\u0438\\u0432\\u0435\\u0442",
                new String(Files.readAllBytes(target), StandardCharsets.UTF_8).trim());
        assertEquals(1, log.infos.size(), log.infos.toString());
        assertEquals("Converting '" + src.toFile() + "' to '" + target.toFile() + "'", log.infos.get(0));
    }

    @Test
    void honoursIncludesAndExcludes(@TempDir Path temp) throws Exception {
        Path src = temp.resolve("src");
        Path included = writeProperties(src.resolve("included/resource_ru.properties"));
        writeProperties(src.resolve("included/excluded_resource_ru.properties"));
        writeProperties(src.resolve("not_included_resource_ru.properties"));
        RecordingLog log = new RecordingLog();

        Native2AsciiMojo mojo = newMojo(src, temp.resolve("out"), log);
        mojo.includes = new String[] {"included/**"};
        mojo.excludes = new String[] {"**/excluded*"};
        mojo.execute();

        Path out = temp.resolve("out");
        assertTrue(Files.exists(out.resolve("included/resource_ru.properties")), "included file was not converted");
        assertFalse(
                Files.exists(out.resolve("included/excluded_resource_ru.properties")), "excluded file was converted");
        assertFalse(
                Files.exists(out.resolve("not_included_resource_ru.properties")),
                "file outside includes was converted");
        assertEquals(1, log.infos.size(), log.infos.toString());
        assertTrue(log.infos.get(0).startsWith("Converting '" + included.toFile() + "'"), log.infos.get(0));
    }

    @Test
    void missingSourceDirectoryWarnsAndConvertsNothing(@TempDir Path temp) throws Exception {
        RecordingLog log = new RecordingLog();
        Path missing = temp.resolve("src/main/native2ascii");

        Native2AsciiMojo mojo = newMojo(missing, temp.resolve("classes"), log);
        mojo.execute();

        assertEquals(1, log.warns.size(), log.warns.toString());
        assertTrue(log.warns.get(0).startsWith("Source directory does not exist: "), log.warns.get(0));
        assertTrue(log.infos.isEmpty(), log.infos.toString());
        assertFalse(Files.exists(temp.resolve("classes")), "target directory was created");
    }

    private static Native2AsciiMojo newMojo(Path srcDir, Path targetDir, RecordingLog log) {
        Native2AsciiMojo mojo = new Native2AsciiMojo();
        mojo.encoding = "UTF-8";
        mojo.srcDir = srcDir.toFile();
        mojo.targetDir = targetDir.toFile();
        mojo.setLog(log);
        return mojo;
    }

    private static Path writeProperties(Path file) throws IOException {
        Files.createDirectories(file.getParent());
        Files.write(file, "hello=Привет\n".getBytes(StandardCharsets.UTF_8));
        return file;
    }

    private static final class RecordingLog extends SystemStreamLog {
        final List<String> infos = new ArrayList<>();
        final List<String> warns = new ArrayList<>();

        @Override
        public void info(CharSequence content) {
            infos.add(content.toString());
        }

        @Override
        public void warn(CharSequence content) {
            warns.add(content.toString());
        }
    }

    private File getSourceDirectory() throws URISyntaxException {
        return new File(Native2AsciiMojoTest.class
                        .getClassLoader()
                        .getResource("xxx.properties")
                        .toURI())
                .getParentFile();
    }

    private Properties loadFile(final File file) throws IOException {
        final Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
            return properties;
        }
    }
}
