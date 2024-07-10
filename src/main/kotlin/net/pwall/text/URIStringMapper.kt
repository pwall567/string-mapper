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

import net.pwall.text.StringMapper.buildResult
import net.pwall.text.StringMapper.fromHexDigit
import net.pwall.text.StringMapper.mapCharacters
import net.pwall.text.StringMapper.mapSubstrings
import net.pwall.util.IntOutput.append2Hex

/**
 * String mapping functions to perform URI percent-encoding and -decoding.
 *
 * @author  Peter Wall
 */
object URIStringMapper {

    private val spaceResult = CharMapResult(1, ' ')

    @Suppress("ConstPropertyName")
    private const val illegalMessage = "Illegal URI escape sequence"
    @Suppress("ConstPropertyName")
    private const val incompleteMessage = "Incomplete URI escape sequence"

    /**
     * Encode string using URI percent-encoding.
     */
    fun String.encodeURI(): String = mapCharacters {
        if (it.isUnreservedForURI() || it == '$') null else StringBuilder(3).apply {
            append('%')
            append2Hex(this, it.code)
        }
    }

    /**
     * Decode string from URI percent-encoding (also decodes "+" as space, despite the fact that the above function does
     * not encode space this way).
     */
    fun String.decodeURI(): String = mapSubstrings {
        when (this[it]) {
            '%' -> buildResult(this, it, 3, incompleteMessage) {
                try {
                    (this[it + 1].fromHexDigit() shl 4) or this[it + 2].fromHexDigit()
                } catch (_: NumberFormatException) {
                    throw IllegalArgumentException(illegalMessage)
                }
            }
            '+' -> spaceResult
            else -> null
        }
    }

    /**
     * Test whether a character is in the "unreserved for URI" set.
     */
    fun Char.isUnreservedForURI(): Boolean = this in 'A'..'Z' || this in 'a'..'z' || this in '0'..'9' ||
            this == '-' || this == '.' || this == '_' || this == '~'

}
