/*
 * @(#) UTF8StringMapper.kt
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
import net.pwall.text.StringMapper.mapCharacters
import net.pwall.text.StringMapper.mapSubstrings

/**
 * String mapping functions to encode and decode UTF-8.
 *
 * @author  Peter Wall
 */
object UTF8StringMapper {

    private const val illegalMessage = "Illegal UTF-8 sequence"
    private const val incompleteMessage = "Incomplete UTF-8 sequence"

    /**
     * Encode string into UTF-8.
     */
    fun String.encodeUTF8(): String = mapCharacters {
        val code = it.code
        if (code <= 0x7F)
            null
        else if (code <= 0x7FF) {
            StringBuilder(2).apply {
                append((0xC0 or (code shr 6)).toChar())
                append((0x80 or (code and 0x3F)).toChar())
            }
        }
        else {
            StringBuilder(3).apply {
                append((0xE0 or (code ushr 12)).toChar())
                append((0x80 or ((code shr 6) and 0x3F)).toChar())
                append((0x80 or (code and 0x3F)).toChar())
            }
        }
    }

    /**
     * Decode string from UTF-8.
     */
    fun String.decodeUTF8(): String = mapSubstrings {
        val first = this[it].code
        if (first <= 0x7F)
            null
        else if (first <= 0xDF) {
            buildResult(this, it, 2, incompleteMessage) {
                val second = this[it + 1].code
                require((second and 0xC0) == 0x80) { illegalMessage }
                ((first and 0x1F) shl 6) or (second and 0x3F)
            }
        }
        else {
            buildResult(this, it, 3, incompleteMessage) {
                val second = this[it + 1].code
                val third = this[it + 2].code
                require((second and 0xC0) == 0x80 && (third and 0xC0) == 0x80) { illegalMessage }
                ((first and 0xF) shl 12) or ((second and 0x3F) shl 6) or (third and 0x3F)
            }
        }
    }

}
