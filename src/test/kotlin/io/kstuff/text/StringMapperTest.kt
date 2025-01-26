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

package io.kstuff.text

import kotlin.test.Test

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldBeSameInstance
import io.kstuff.test.shouldThrow

import io.kstuff.text.StringMapper.checkLength
import io.kstuff.text.StringMapper.fromHexDigit
import io.kstuff.text.StringMapper.mapCharacters
import io.kstuff.text.StringMapper.mapSubstrings

class StringMapperTest {

    @Test fun `should map characters correctly`() {
        val unchanged = "unchanged"
        unchanged.mapCharacters { null } shouldBe unchanged
        "a/b".mapCharacters { if (it == '~') "~0" else if (it == '/') "~1" else null } shouldBe "a~1b"
        "a/~b".mapCharacters { if (it == '~') "~0" else if (it == '/') "~1" else null } shouldBe "a~1~0b"
    }

    @Test fun `should map substrings correctly`() {
        val unchanged = "unchanged"
        unchanged.mapSubstrings { null } shouldBeSameInstance  unchanged
        unchanged.mapSubstrings { if (unchanged[it] in "hnu") ElisionMapResult(1) else null } shouldBe "caged"
        unchanged.mapSubstrings {
            if (unchanged.regionMatches(it, "ang", 0, 3))
                StringMapResult(3, "eck")
            else
                null
        } shouldBe "unchecked"
    }

    @Test fun `should check whether string contains required number of characters`() {
        val data = "data"
        checkLength(data, 0, 2)
        checkLength(data, 0, 4)
        checkLength(data, 3, 1)
        shouldThrow<IllegalArgumentException>("Incomplete escape sequence") { checkLength(data, 0, 5) }
        shouldThrow<IllegalArgumentException>("Incomplete escape sequence") { checkLength(data, 3, 2) }
        shouldThrow<IllegalArgumentException>("message") { checkLength(data, 3, 2, "message") }
    }

    @Test fun `should convert hex digits`() {
        '0'.fromHexDigit() shouldBe 0
        '1'.fromHexDigit() shouldBe 1
        '2'.fromHexDigit() shouldBe 2
        '3'.fromHexDigit() shouldBe 3
        '4'.fromHexDigit() shouldBe 4
        '5'.fromHexDigit() shouldBe 5
        '6'.fromHexDigit() shouldBe 6
        '7'.fromHexDigit() shouldBe 7
        '8'.fromHexDigit() shouldBe 8
        '9'.fromHexDigit() shouldBe 9
        'A'.fromHexDigit() shouldBe 10
        'B'.fromHexDigit() shouldBe 11
        'C'.fromHexDigit() shouldBe 12
        'D'.fromHexDigit() shouldBe 13
        'E'.fromHexDigit() shouldBe 14
        'F'.fromHexDigit() shouldBe 15
        'a'.fromHexDigit() shouldBe 10
        'b'.fromHexDigit() shouldBe 11
        'c'.fromHexDigit() shouldBe 12
        'd'.fromHexDigit() shouldBe 13
        'e'.fromHexDigit() shouldBe 14
        'f'.fromHexDigit() shouldBe 15
    }

    @Test fun `should fail on invalid hex digit`() {
        shouldThrow<NumberFormatException>("Illegal hexadecimal digit") { 'G'.fromHexDigit() }
        shouldThrow<NumberFormatException>("Illegal hexadecimal digit") { '.'.fromHexDigit() }
    }

}
