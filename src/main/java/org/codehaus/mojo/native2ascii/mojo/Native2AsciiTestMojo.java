/**
 *
 */
package org.codehaus.mojo.native2ascii.mojo;

import java.io.File;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Converts files with characters in any supported character encoding to one with ASCII and/or
 * Unicode escapes.
 *
 * @author David Matějček
 */
@Mojo(name = "testResources", defaultPhase = LifecyclePhase.PROCESS_TEST_RESOURCES)
public class Native2AsciiTestMojo extends Native2AsciiMojo {

  /**
   * Target directory.
   */
  @Parameter(defaultValue = "${project.build.testOutputDirectory}")
  public File targetDir;

  /**
   * Source directory.
   */
  @Parameter(defaultValue = "src/test/native2ascii")
  public File srcDir;


  @Override
  protected File getSourceDirectory() {
    return srcDir;
  }


  /**
   * @return the target directory
   */
  @Override
  protected File getTargetDirectory() {
    return targetDir;
  }
}
