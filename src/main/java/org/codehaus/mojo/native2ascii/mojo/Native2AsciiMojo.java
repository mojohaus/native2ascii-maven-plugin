/*
 * The MIT License
 * Copyright (c) 2014-2023 MojoHaus
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
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.native2ascii.Native2Ascii;

/**
 * Converts files with characters in any supported character encoding to one with ASCII and/or
 * Unicode escapes.
 */
@Mojo(name = "resources", threadSafe = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class Native2AsciiMojo extends AbstractNative2AsciiMojo {

    /**
     * Target directory.
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}")
    public File targetDir;

    /**
     * Source directory.
     */
    @Parameter(defaultValue = "src/main/native2ascii")
    public File srcDir;

    @Override
    protected File getSourceDirectory() {
        return srcDir;
    }

    /**
     * @return the target directory
     */
    protected File getTargetDirectory() {
        return targetDir;
    }

    @Override
    public void executeTransformation(final Iterator<File> files) throws MojoExecutionException {
        if (!getTargetDirectory().exists()) {
            getTargetDirectory().mkdirs();
        }

        while (files.hasNext()) {
            final File file = files.next();
            try {
                File targetFile = getTargetFile(file);
                getLog().info("Converting '" + file + "' to '" + targetFile + "'");
                new Native2Ascii(getLog()).nativeToAscii(file, targetFile, encoding);
            } catch (final IOException e) {
                throw new MojoExecutionException("Unable to convert " + file.getAbsolutePath(), e);
            }
        }
    }

    private File getTargetFile(final File sourceFile) throws IOException {
        return new File(StringUtils.replace(
                sourceFile.getCanonicalPath(),
                getSourceDirectory().getCanonicalPath(),
                getTargetDirectory().getCanonicalPath()));
    }
}
