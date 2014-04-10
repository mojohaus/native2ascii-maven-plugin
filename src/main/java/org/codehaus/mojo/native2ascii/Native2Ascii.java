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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;

import org.apache.commons.lang3.text.translate.CodePointTranslator;
import org.apache.maven.plugin.logging.Log;

/**
 * @author Evgeny Mandrikov
 */
public final class Native2Ascii {

  private final Log log;
  private final CodePointTranslator escaper;


  /**
   * @param log - used for logging
   */
  public Native2Ascii(final Log log) {
    this.log = log;
    this.escaper = new PropertyEscaper();
  }


  /**
   * Converts given CharSequence into unicode escaped ASCII String.
   *
   * @param string - native string
   * @return unicode escaped string
   */
  public String nativeToAscii(final String string) {
    log.debug("Converting: " + string);
    if (string == null) {
      return null;
    }
    return escaper.translate(string);
  }


  /**
   * Converts given file into file unicode escaped ASCII file.
   *
   * @param src
   * @param dst
   * @param encoding
   * @throws IOException
   */
  public void nativeToAscii(final File src, final File dst, final String encoding) throws IOException {
    log.info("Converting: '" + src + "' to: '" + dst + "'");
    BufferedReader input = null;
    BufferedWriter output = null;
    try {
      if (!dst.getParentFile().exists()) {
        dst.getParentFile().mkdirs();
      }
      input = new BufferedReader(new InputStreamReader(new FileInputStream(src), encoding));
      output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst), "ISO-8859-1"));

      final char[] buffer = new char[4096];
      int len;
      while ((len = input.read(buffer)) != -1) {
        output.write(nativeToAscii(CharBuffer.wrap(buffer, 0, len).toString()));
      }
    } finally {
      closeQuietly(src, input);
      closeQuietly(dst, output);
    }
  }


  private void closeQuietly(final File file, final Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (final IOException e) {
        log.warn("Could not close the file: " + file.getAbsolutePath());
      }
    }
  }

}
