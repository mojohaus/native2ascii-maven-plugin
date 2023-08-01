/*
 * The MIT License
 * Copyright (c) 2023 MojoHaus
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.native2ascii.Native2Ascii;
import org.codehaus.plexus.util.FileUtils;

/**
 * Converts files with characters in any supported character encoding to one with ASCII and/or
 * Unicode escapes.
 * <p>
 * This mojo converts files inplace, it is not recommended to execute it on src subdirectories.
 */
@Mojo(name = "inplace", threadSafe = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class Native2AsciiInplaceMojo extends AbstractNative2AsciiMojo {

    /**
     * Both source and target directory.
     */
    @Parameter(required = true, defaultValue = "${native2ascii.dir}")
    protected File dir;

    /**
     * The project's target directory.
     */
    @Parameter(required = true, readonly = true, property = "project.build.directory")
    private File targetDir;

    @Override
    protected File getSourceDirectory() {
        return dir;
    }

    @Override
    public void executeTransformation(final Iterator<File> files) throws MojoExecutionException {
        Path tmpDir = createTmpDir();
        while (files.hasNext()) {
            File file = files.next();
            getLog().info("Converting " + file);
            try {
                File tempFile = Files.createTempFile(tmpDir, file.getName(), "native2ascii")
                        .toFile();
                new Native2Ascii(getLog()).nativeToAscii(file, tempFile, encoding);
                FileUtils.rename(tempFile, file);
                getLog().debug("File converted successfuly: " + file);
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to convert " + file.getAbsolutePath(), e);
            }
        }
    }

    private Path createTmpDir() throws MojoExecutionException {
        try {
            return Files.createTempDirectory(targetDir.toPath(), "tmp");
        } catch (IOException e) {
            throw new MojoExecutionException("Could not create temporary directory under the " + targetDir, e);
        }
    }
}
