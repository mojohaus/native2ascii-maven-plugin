/*
 * The MIT License
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
@Mojo(name = "inplace", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class Native2AsciiInplaceMojo extends AbstractNative2AsciiMojo {

  /**
   * Both source and target directory.
   */
  @Parameter(required = true, defaultValue = "${native2ascii.dir}")
  protected File dir;


  @Override
  protected File getSourceDirectory() {
    return dir;
  }


  @Override
  public void executeTransformation(final Iterator<File> files) throws MojoExecutionException {
    while (files.hasNext()) {
      File file = files.next();
      getLog().info("Processing " + file.getAbsolutePath());
      try {
        // Convert file in-place
        File tempFile = File.createTempFile(file.getName(), "native2ascii");
        new Native2Ascii(getLog()).nativeToAscii(file, tempFile, encoding);
        FileUtils.rename(tempFile, file);
        getLog().info("File converted successfuly: " + file.getAbsolutePath());
      } catch (IOException e) {
        throw new MojoExecutionException("Unable to convert " + file.getAbsolutePath(), e);
      }
    }
  }
}
