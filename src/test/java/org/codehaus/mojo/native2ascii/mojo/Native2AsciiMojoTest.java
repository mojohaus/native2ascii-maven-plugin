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
import java.util.Properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author David Matějček
 */
public class Native2AsciiMojoTest {

    @Test
    public void testFile() throws Exception {
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
    public void testSubdirectories() throws Exception {
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

    private File getSourceDirectory() throws URISyntaxException {
        return new File(Native2AsciiMojoTest.class
                        .getClassLoader()
                        .getResource("xxx.properties")
                        .toURI())
                .getParentFile();
    }

    private Properties loadFile(final File file) throws IOException {
        final Properties properties = new Properties();
        final FileInputStream inputStream = new FileInputStream(file);
        try {
            properties.load(inputStream);
            return properties;
        } finally {
            inputStream.close();
        }
    }
}
