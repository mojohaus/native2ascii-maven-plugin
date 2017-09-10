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

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author David Matějček
 */
public abstract class AbstractNative2AsciiMojo extends AbstractMojo {

  /**
   * The native encoding the files are in.
   */
  @Parameter(defaultValue = "${project.build.sourceEncoding}")
  public String encoding;

  /**
   * Patterns of files to include. Default is "**\/*.properties".
   */
  @Parameter
  public String[] includes;

  /**
   * Patterns of files that must be excluded.
   */
  @Parameter
  public String[] excludes;


  public final void execute() throws MojoExecutionException, MojoFailureException {
    if (!checkParameters()) {
      return;
    }
    final Iterator<File> files = findFiles();
    executeTransformation(files);
  }


  /**
   * @return the root directory where the plugin should look for files.
   */
  protected abstract File getSourceDirectory();


  /**
   * Executes the transformation of files.
   *
   * @param files
   * @throws MojoExecutionException
   */
  protected abstract void executeTransformation(Iterator<File> files) throws MojoExecutionException;


  /**
   * Checks all attributes. Override if needed.
   *
   * @return true if we can continue
   */
  protected boolean checkParameters() {
    if (!getSourceDirectory().exists()) {
      getLog().warn("Source directory does not exist: " + getSourceDirectory().getAbsolutePath());
      return false;
    }
    if (includes == null) {
      includes = new String[] {"**/*.properties"};
    }
    if (excludes == null) {
      excludes = new String[0];
    }
    if (StringUtils.isEmpty(encoding)) {
      encoding = System.getProperty("file.encoding");
      getLog().warn("Using platform encoding (" + encoding + " actually) to convert resources!");
    }
    return true;
  }


  private Iterator<File> findFiles() throws MojoExecutionException {
    try {
      if (getLog().isInfoEnabled()) {
        getLog().info("Includes: " + Arrays.asList(includes));
        getLog().info("Excludes: " + Arrays.asList(excludes));
      }
      String incl = StringUtils.join(includes, ",");
      String excl = StringUtils.join(excludes, ",");
      @SuppressWarnings("unchecked")
      final Iterator<File> files = FileUtils.getFiles(getSourceDirectory(), incl, excl).iterator();
      return files;
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to get list of files");
    }
  }

}
