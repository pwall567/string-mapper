/*
 * @(#) URIStringMapper.kt
 *
 * string-mapper  String mapping utilities
 * Copyright (c) 2022 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.pwall.text

import net.pwall.text.StringMapper.mapCharacters
import net.pwall.text.StringMapper.mapSubstring
import net.pwall.util.IntOutput.append2Hex

/**
 * String mapping functions to perform URI percent-encoding and decoding.
 *
 * @author  Peter Wall
 */
object URIStringMapper {

    /**
     * Encode string using URI percent-encoding.
     */
    fun String.encodeURI() = mapCharacters {
        if (it.isUnreservedForURI() || it == '$') null else StringBuilder(3).apply {
            append('%')
            append2Hex(this, it.code)
        }
    }

    /**
     * Decode string from URI percent-encoding.
     */
    fun String.decodeURI() = mapSubstring { s, i ->
        if (s[i] != '%') null else StringMapper.buildResult(s, i, 3) {
            (s[i + 1].fromHexDigit() shl 4) or s[i + 2].fromHexDigit()
        }
    }

    fun Char.isUnreservedForURI(): Boolean = this in 'A'..'Z' || this in 'a'..'z' || this in '0'..'9' ||
            this == '-' || this == '.' || this == '_' || this == '~'

    fun Char.fromHexDigit(): Int = when (this) {
        in '0'..'9' -> this.code - '0'.code
        in 'A'..'F' -> this.code - 'A'.code + 10
        in 'a'..'f' -> this.code - 'a'.code + 10
        else -> throw IllegalArgumentException("Illegal URI escape sequence")
    }

}
