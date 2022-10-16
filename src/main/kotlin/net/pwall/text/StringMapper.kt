/*
 * @(#) StringMapper.kt
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

/**
 * A set of mapping functions to assist with encoding and decoding strings to and from an "escaped" form.
 *
 * @author  Peter Wall
 */
object StringMapper {

    /**
     * Map characters in a string to an encoded form (usually multi-character).  The map function takes the character to
     * be mapped, and returns:
     *
     * - `null`, to indicate that the character is not to be mapped
     * - a replacement string (as a `CharSequence`), may be empty
     *
     * If no characters require to be mapped, the original string is returned unchanged.
     */
    fun String.mapCharacters(mapFunction: (Char) -> CharSequence?): String {
        val len = length
        for (i in 0 until len) {
            mapFunction(get(i))?.let {
                return buildString {
                    append(this@mapCharacters, 0, i)
                    append(it)
                    for (j in i + 1 until len) {
                        val ch = this@mapCharacters[j]
                        mapFunction(ch)?.let { append(it) } ?: append(ch)
                    }
                }
            }
        }
        return this
    }

    /**
     * Map substrings in a string to a decoded form (usually a single character).  The map function takes two
     * parameters, the original string and the current index, and returns a `MapResult` object which contains the
     * replacement details and the number of characters from the original string to be skipped.
     */
    fun String.mapSubstring(mapFunction: (String, Int) -> MapResult?): String {
        val len = length
        for (i in 0 until len) {
            mapFunction(this, i)?.let {
                return buildString {
                    append(this@mapSubstring, 0, i)
                    it.appendResult(this)
                    var j = i + it.length
                    while (j < len) {
                        mapFunction(this@mapSubstring, j)?.let {
                            it.appendResult(this)
                            j += it.length
                        } ?: append(this@mapSubstring[j++])
                    }
                }
            }
        }
        return this
    }

    fun checkLength(s: String, i: Int, len: Int, name: String = "escape") {
        require(i + len <= s.length) { "Incomplete $name sequence" }
    }

    fun buildResult(s: String, i: Int, len: Int, name: String = "escape", block: () -> Int): MapResult {
        checkLength(s, i, len, name)
        return CharMapResult(len, block().toChar())
    }

}
