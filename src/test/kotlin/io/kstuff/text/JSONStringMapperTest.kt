/*
 * @(#) JSONStringMapperTest.kt
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

package io.kstuff.text

import kotlin.test.Test

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldBeSameInstance
import io.kstuff.test.shouldThrow

import io.kstuff.text.JSONStringMapper.decodeJSON
import io.kstuff.text.JSONStringMapper.encodeJSON

class JSONStringMapperTest {

    @Test fun `should return original string when nothing to be encoded`() {
        val unchanged = "unchanged"
        unchanged.encodeJSON() shouldBeSameInstance unchanged
    }

    @Test fun `should encode JSON characters as needed`() {
        "backslash \\ quote \" bs \b ff \u000C lf \n cr \r ht \t".encodeJSON() shouldBe
                "backslash \\\\ quote \\\" bs \\b ff \\f lf \\n cr \\r ht \\t"
        "nul \u0000 em dash \u2014 up arr \u21D1".encodeJSON() shouldBe "nul \\u0000 em dash \\u2014 up arr \\u21d1"
    }

    @Test fun `should return original string when nothing to be decoded`() {
        val unchanged = "unchanged"
        unchanged.decodeJSON() shouldBeSameInstance unchanged
    }

    @Test fun `should decode JSON characters as needed`() {
        "backslash \\\\ quote \\\" bs \\b ff \\f lf \\n cr \\r ht \\t slash \\/".decodeJSON() shouldBe
                "backslash \\ quote \" bs \b ff \u000C lf \n cr \r ht \t slash /"
        "nul \\u0000 em dash \\u2014 up arr \\u21D1".decodeJSON() shouldBe "nul \u0000 em dash \u2014 up arr \u21D1"

    }

    @Test fun `should throw exception on illegal escape sequence`() {
        shouldThrow<IllegalArgumentException>("Illegal JSON escape sequence") {
            "\\a".decodeJSON()
        }
    }

    @Test fun `should throw exception on illegal unicode escape sequence`() {
        shouldThrow<IllegalArgumentException>("Illegal JSON escape sequence") {
            "\\u123g".decodeJSON()
        }
    }

    @Test fun `should throw exception on incomplete escape sequence`() {
        shouldThrow<IllegalArgumentException>("Incomplete JSON escape sequence") {
            "\\u123".decodeJSON()
        }
    }

}
