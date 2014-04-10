/**
 *
 */
package org.codehaus.mojo.native2ascii.mojo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.codehaus.mojo.native2ascii.mojo.Native2AsciiMojo;
import org.junit.Test;


/**
 * @author David Matějček
 */
public class Native2AsciiMojoTest {

  @Test
  public void testFile() throws Exception {
    Native2AsciiMojo mojo = new Native2AsciiMojo();
    mojo.srcDir = new File(Native2AsciiMojoTest.class.getClassLoader().getResource("xxx.properties").toURI()).getParentFile();
    mojo.targetDir = mojo.srcDir.getParentFile();
    mojo.includes = new String[] {"xxx.properties"};
    mojo.excludes = new String[0];
    mojo.execute();

    Properties properties = new Properties();
    final FileInputStream inputStream = new FileInputStream(new File(mojo.targetDir, "xxx.properties"));
    try {
      properties.load(inputStream);
    } finally {
      inputStream.close();
    }
    assertEquals("~", properties.get("1"));
    assertEquals("", properties.get("2"));
    assertNull(properties.get("3"));
    assertEquals("粗 Řízeček utíká a řeže zatáčky! §", properties.get("4"));
    assertNull(properties.get("5"));
    assertEquals("more tests needed!", properties.get("6"));
    assertEquals("", properties.get("7"));
  }

}
