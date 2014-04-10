/**
 *
 */
package org.codehaus.mojo.native2ascii;

import org.apache.commons.lang3.text.translate.UnicodeEscaper;


/**
 * Uses {@link UnicodeEscaper} to translate strings with bytes over \u007F to unicode.
 *
 * @author David Matějček
 */
public class PropertyEscaper extends UnicodeEscaper {

  /**
   * Constructor using {@link UnicodeEscaper#UnicodeEscaper(int, int, boolean)}
   */
  public PropertyEscaper() {
    super(0x007F, Integer.MAX_VALUE, true);
  }

}
