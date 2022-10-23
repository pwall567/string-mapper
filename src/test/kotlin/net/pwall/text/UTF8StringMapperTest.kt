/*
 * @(#) UTF8StringMapperTest.kt
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
import kotlin.test.expect

import net.pwall.text.UTF8StringMapper.decodeUTF8
import net.pwall.text.UTF8StringMapper.encodeUTF8

class UTF8StringMapperTest {

    @Test fun `should map to UTF-8`() {
        expect("") { "".encodeUTF8() }
        expect("[ \u00C2\u00A9 \u00C3\u00B7 ]") { "[ \u00A9 \u00F7 ]".encodeUTF8() }
        expect("[ \u00E2\u0080\u0094 ]") { "[ \u2014 ]".encodeUTF8() }
        expect("[ \u00EF\u00BB\u00BF ]") { "[ \uFEFF ]".encodeUTF8() }
    }

    @Test fun `should map from UTF-8`() {
        expect("") { "".decodeUTF8() }
        expect("[ \u00A9 \u00F7 ]") { "[ \u00C2\u00A9 \u00C3\u00B7 ]".decodeUTF8() }
        expect("[ \u2014 ]") { "[ \u00E2\u0080\u0094 ]".decodeUTF8() }
        expect("[ \uFEFF ]") { "[ \u00EF\u00BB\u00BF ]".decodeUTF8() }
    }

    @Test fun `should fail on incomplete UTF-8 sequence`() {
        assertFailsWith<IllegalArgumentException> { " \u00C2".decodeUTF8() }.let {
            expect("Incomplete UTF-8 sequence") { it.message }
        }
        assertFailsWith<IllegalArgumentException> { " \u00E2\u0080".decodeUTF8() }.let {
            expect("Incomplete UTF-8 sequence") { it.message }
        }
    }

    @Test fun `should fail on illegal UTF-8 sequence`() {
        assertFailsWith<IllegalArgumentException> { " \u00C2\u0000".decodeUTF8() }.let {
            expect("Illegal UTF-8 sequence") { it.message }
        }
        assertFailsWith<IllegalArgumentException> { " \u00E2\u0080\u0030".decodeUTF8() }.let {
            expect("Illegal UTF-8 sequence") { it.message }
        }
    }

}
