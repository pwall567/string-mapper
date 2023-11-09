/*
 * @(#) JSONStringMapper.kt
 *
 * string-mapper  String mapping utilities
 * Copyright (c) 2022, 2023 Peter Wall
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
import net.pwall.util.IntOutput

/**
 * String mapping functions to perform JSON string encoding and decoding.
 *
 * @author  Peter Wall
 */
object JSONStringMapper {

    private val backSlash = CharMapResult(2, '\\')
    private val doubleQuote = CharMapResult(2, '"')
    private val slash = CharMapResult(2, '/')
    private val backSpace = CharMapResult(2, '\b')
    private val formFeed = CharMapResult(2, '\u000c')
    private val newLine = CharMapResult(2, '\n')
    private val carriageReturn = CharMapResult(2, '\r')
    private val tab = CharMapResult(2, '\t')

    private const val illegalMessage = "Illegal JSON escape sequence"
    private const val incompleteMessage = "Incomplete JSON escape sequence"

    /**
     * Encode a string using JSON string escaping.
     */
    fun String.encodeJSON(): String = mapCharacters {
        when (it) {
            '\\' -> "\\\\"
            '"' -> "\\\""
            in ' '..'~' -> null
            '\b' -> "\\b"
            '\u000c' -> "\\f"// TODO drop this and allow it to encode as \u000C ?
            '\n' -> "\\n"
            '\r' -> "\\r"
            '\t' -> "\\t"
            else -> buildString {
                append('\\')
                append('u')
                IntOutput.append4HexLC(this, it.code)
            }
        }
    }

    /**
     * Decode a string from JSON string escaping.
     */
    fun String.decodeJSON(): String = mapSubstrings {
        when (this[it]) {
            '\\' -> when (this[it + 1]) {
                '\\' -> backSlash
                '"' -> doubleQuote
                '/' -> slash
                'b' -> backSpace
                'f' -> formFeed
                'n' -> newLine
                'r' -> carriageReturn
                't' -> tab
                'u' -> buildResult(this, it, 6, incompleteMessage) {
                    try {
                        (this[it + 2].fromHexDigit() shl 12) or (this[it + 3].fromHexDigit() shl 8) or
                                (this[it + 4].fromHexDigit() shl 4) or this[it + 5].fromHexDigit()
                    } catch (_: NumberFormatException) {
                        throw IllegalArgumentException(illegalMessage)
                    }
                }
                else -> throw IllegalArgumentException(illegalMessage)
            }
            else -> null
        }
    }

}
