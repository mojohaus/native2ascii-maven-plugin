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
package org.codehaus.mojo.native2ascii;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Native2AsciiTest {

    private final Native2Ascii native2Ascii = new Native2Ascii(new SystemStreamLog());

    @Test
    public void test() {
        assertEquals(null, this.native2Ascii.nativeToAscii(null));
        assertEquals("~", this.native2Ascii.nativeToAscii("~"));
        assertEquals("\\u007F", this.native2Ascii.nativeToAscii(""));
        // chinese character is random since I don't understand it
        assertEquals("\\u7C97", this.native2Ascii.nativeToAscii("粗"));
        assertEquals(
                "\\u0158\\u00EDze\\u010Dek ut\\u00EDk\\u00E1 a \\u0159e\\u017Ee zat\\u00E1\\u010Dky! \\u00A7",
                this.native2Ascii.nativeToAscii("Řízeček utíká a řeže zatáčky! §"));
    }
}
