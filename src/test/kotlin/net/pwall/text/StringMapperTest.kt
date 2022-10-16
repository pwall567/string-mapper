/*
 * @(#) StringMapperTest.kt
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

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.expect

import net.pwall.text.StringMapper.checkLength
import net.pwall.text.StringMapper.mapCharacters
import net.pwall.text.StringMapper.mapSubstring

class StringMapperTest {

    @Test fun `should map characters correctly`() {
        val unchanged = "unchanged"
        assertSame(unchanged, unchanged.mapCharacters { null })
        expect("a~1b") { "a/b".mapCharacters { if (it == '~') "~0" else if (it == '/') "~1" else null } }
        expect("a~1~0b") { "a/~b".mapCharacters { if (it == '~') "~0" else if (it == '/') "~1" else null } }
    }

    @Test fun `should map substrings correctly`() {
        val unchanged = "unchanged"
        assertSame(unchanged, unchanged.mapSubstring { null })
        expect("caged") { unchanged.mapSubstring { if (unchanged[it] in "hnu") ElisionMapResult(1) else null } }
        expect("unchecked") { unchanged.mapSubstring {
            if (unchanged.regionMatches(it, "ang", 0, 3))
                StringMapResult(3, "eck")
            else
                null
        }}
    }

    @Test fun `should check whether string contains required number of characters`() {
        val data = "data"
        checkLength(data, 0, 2)
        checkLength(data, 0, 4)
        checkLength(data, 3, 1)
        assertFailsWith<IllegalArgumentException> { checkLength(data, 0, 5) }.let {
            expect("Incomplete escape sequence") { it.message }
        }
        assertFailsWith<IllegalArgumentException> { checkLength(data, 3, 2) }.let {
            expect("Incomplete escape sequence") { it.message }
        }
        assertFailsWith<IllegalArgumentException> { checkLength(data, 3, 2, "encoded") }.let {
            expect("Incomplete encoded sequence") { it.message }
        }
    }

}
