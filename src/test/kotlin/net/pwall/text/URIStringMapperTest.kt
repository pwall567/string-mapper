/*
 * @(#) URIStringMapperTest.kt
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
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.test.expect
import net.pwall.text.URIStringMapper.decodeURI

import net.pwall.text.URIStringMapper.encodeURI
import net.pwall.text.URIStringMapper.fromHexDigit
import net.pwall.text.URIStringMapper.isUnreservedForURI

class URIStringMapperTest {

    @Test fun `should encode URI string`() {
        val unchanged = "unchanged"
        assertSame(unchanged, unchanged.encodeURI())
        expect("a%2Fb") { "a/b".encodeURI() }
        expect("%28%3F%29") { "(?)".encodeURI() }
    }

    @Test fun `should decode URI string`() {
        val unchanged = "unchanged"
        assertSame(unchanged, unchanged.decodeURI())
        expect("a/b") { "a%2Fb".decodeURI() }
        expect("(?)") { "%28%3F%29".decodeURI() }
    }

    @Test fun `should convert hex digits`() {
        expect(0) { '0'.fromHexDigit() }
        expect(1) { '1'.fromHexDigit() }
        expect(8) { '8'.fromHexDigit() }
        expect(9) { '9'.fromHexDigit() }
        expect(10) { 'A'.fromHexDigit() }
        expect(11) { 'B'.fromHexDigit() }
        expect(14) { 'E'.fromHexDigit() }
        expect(15) { 'F'.fromHexDigit() }
        expect(10) { 'a'.fromHexDigit() }
        expect(11) { 'b'.fromHexDigit() }
        expect(14) { 'e'.fromHexDigit() }
        expect(15) { 'f'.fromHexDigit() }
    }

    @Test fun `should fail on invalid hex digit`() {
        assertFailsWith<IllegalArgumentException> { 'G'.fromHexDigit() }.let {
            expect("Illegal URI escape sequence") { it.message }
        }
        assertFailsWith<IllegalArgumentException> { '.'.fromHexDigit() }.let {
            expect("Illegal URI escape sequence") { it.message }
        }
    }

    @Test fun `should determine whether character is unreserved for URI`() {
        assertTrue('A'.isUnreservedForURI())
        assertTrue('B'.isUnreservedForURI())
        assertTrue('Y'.isUnreservedForURI())
        assertTrue('Z'.isUnreservedForURI())
        assertTrue('a'.isUnreservedForURI())
        assertTrue('b'.isUnreservedForURI())
        assertTrue('y'.isUnreservedForURI())
        assertTrue('z'.isUnreservedForURI())
        assertTrue('0'.isUnreservedForURI())
        assertTrue('1'.isUnreservedForURI())
        assertTrue('8'.isUnreservedForURI())
        assertTrue('9'.isUnreservedForURI())
        assertTrue('-'.isUnreservedForURI())
        assertTrue('.'.isUnreservedForURI())
        assertTrue('_'.isUnreservedForURI())
        assertTrue('~'.isUnreservedForURI())
        assertFalse('!'.isUnreservedForURI())
        assertFalse('"'.isUnreservedForURI())
        assertFalse('#'.isUnreservedForURI())
        assertFalse('$'.isUnreservedForURI())
        assertFalse('%'.isUnreservedForURI())
        assertFalse('&'.isUnreservedForURI())
        assertFalse('\''.isUnreservedForURI())
        assertFalse('('.isUnreservedForURI())
        assertFalse(')'.isUnreservedForURI())
        assertFalse('*'.isUnreservedForURI())
        assertFalse('+'.isUnreservedForURI())
        assertFalse(','.isUnreservedForURI())
        assertFalse('/'.isUnreservedForURI())
        assertFalse(':'.isUnreservedForURI())
        assertFalse(';'.isUnreservedForURI())
        assertFalse('<'.isUnreservedForURI())
        assertFalse('='.isUnreservedForURI())
        assertFalse('>'.isUnreservedForURI())
        assertFalse('?'.isUnreservedForURI())
        assertFalse('@'.isUnreservedForURI())
        assertFalse('['.isUnreservedForURI())
        assertFalse('\\'.isUnreservedForURI())
        assertFalse(']'.isUnreservedForURI())
        assertFalse('^'.isUnreservedForURI())
        assertFalse('`'.isUnreservedForURI())
        assertFalse('{'.isUnreservedForURI())
        assertFalse('|'.isUnreservedForURI())
        assertFalse('}'.isUnreservedForURI())
    }

}
