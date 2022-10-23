/*
 * @(#) JSONStringMapperTest.kt
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

import net.pwall.text.JSONStringMapper.decodeJSON
import net.pwall.text.JSONStringMapper.encodeJSON

class JSONStringMapperTest {

    @Test fun `should return original string when nothing to be encoded`() {
        val unchanged = "unchanged"
        assertSame(unchanged, unchanged.encodeJSON())
    }

    @Test fun `should encode JSON characters as needed`() {
        expect("backslash \\\\ quote \\\" bs \\b ff \\f lf \\n cr \\r ht \\t") {
            "backslash \\ quote \" bs \b ff \u000C lf \n cr \r ht \t".encodeJSON()
        }
        expect("nul \\u0000 em dash \\u2014") { "nul \u0000 em dash \u2014".encodeJSON() }
    }

    @Test fun `should return original string when nothing to be decoded`() {
        val unchanged = "unchanged"
        assertSame(unchanged, unchanged.decodeJSON())
    }

    @Test fun `should decode JSON characters as needed`() {
        expect("backslash \\ quote \" bs \b ff \u000C lf \n cr \r ht \t slash /") {
            "backslash \\\\ quote \\\" bs \\b ff \\f lf \\n cr \\r ht \\t slash \\/".decodeJSON()
        }
        expect("nul \u0000 em dash \u2014") { "nul \\u0000 em dash \\u2014".decodeJSON() }
    }

    @Test fun `should throw exception on illegal escape sequence`() {
        assertFailsWith<IllegalArgumentException> { "\\a".decodeJSON() }.let {
            expect("Illegal JSON escape sequence") { it.message }
        }
    }

    @Test fun `should throw exception on illegal unicode escape sequence`() {
        assertFailsWith<IllegalArgumentException> { "\\u123g".decodeJSON() }.let {
            expect("Illegal JSON escape sequence") { it.message }
        }
    }

    @Test fun `should throw exception on incomplete escape sequence`() {
        assertFailsWith<IllegalArgumentException> { "\\u123".decodeJSON() }.let {
            expect("Incomplete JSON escape sequence") { it.message }
        }
    }

}
