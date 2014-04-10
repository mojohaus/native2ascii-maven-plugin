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
package org.codehaus.mojo.native2ascii;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

/**
 * Converts files with characters in any supported character encoding to one with ASCII and/or
 * Unicode escapes.
 */
@Mojo(name = "inplace", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class Native2AsciiInplaceMojo extends AbstractMojo {

  /**
   * Directory.
   */
  @Parameter(required = true, defaultValue = "${native2ascii.dir}")
  protected File dir;

  /**
   * The native encoding the files are in.
   */
  @Parameter(defaultValue = "${project.build.sourceEncoding}")
  protected String encoding;

  /**
   * Patterns of files to include. Default is "**\/*.properties".
   */
  @Parameter
  protected String[] includes;

  /**
   * Patterns of files that must be excluded.
   */
  @Parameter
  protected String[] excludes;


  public void execute() throws MojoExecutionException, MojoFailureException {
    if (StringUtils.isEmpty(encoding)) {
      encoding = System.getProperty("file.encoding");
      getLog().warn("Using platform encoding (" + encoding + " actually) to convert resources!");
    }

    if (!dir.exists()) {
      getLog().warn("The directory '" + dir + "' does not exist!");
      return;
    }

    if (includes == null) {
      includes = new String[] {"**/*.properties"};
    }
    if (excludes == null) {
      excludes = new String[0];
    }

    final Iterator<File> files = findFiles();
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


  private Iterator<File> findFiles() throws MojoExecutionException {

    try {
      getLog().info("Includes: " + Arrays.asList(includes));
      getLog().info("Excludes: " + Arrays.asList(excludes));
      String incl = StringUtils.join(includes, ",");
      String excl = StringUtils.join(excludes, ",");
      @SuppressWarnings("unchecked")
      final Iterator<File> files = FileUtils.getFiles(dir, incl, excl).iterator();
      return files;
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to get list of files");
    }
  }
}
