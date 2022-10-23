/*
 * @(#) MapResult.kt
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
 * Result object for [StringMapper].  Provides the replacement text, and the number of input characters to be skipped.
 *
 * @author  Peter Wall
 */
interface MapResult {
    val length: Int
    fun appendResult(a: Appendable)
}

class StringMapResult(override val length: Int, private val result: String) : MapResult {
    override fun appendResult(a: Appendable) {
        a.append(result)
    }
}

class CharMapResult(override val length: Int, private val result: Char) : MapResult {
    override fun appendResult(a: Appendable) {
        a.append(result)
    }
}

class ElisionMapResult(override val length: Int) : MapResult {
    override fun appendResult(a: Appendable) {}
}
