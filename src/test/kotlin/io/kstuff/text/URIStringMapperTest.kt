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

package io.kstuff.text

import kotlin.test.Test

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldBeSameInstance

import io.kstuff.text.URIStringMapper.decodeURI
import io.kstuff.text.URIStringMapper.encodeURI
import io.kstuff.text.URIStringMapper.isUnreservedForURI

class URIStringMapperTest {

    @Test fun `should encode URI string`() {
        val unchanged = "unchanged"
        unchanged.encodeURI() shouldBeSameInstance unchanged
        "a/b".encodeURI() shouldBe "a%2Fb"
        "(?)".encodeURI() shouldBe "%28%3F%29"
        "a b".encodeURI() shouldBe "a%20b"
    }

    @Test fun `should decode URI string`() {
        val unchanged = "unchanged"
        unchanged.decodeURI() shouldBe unchanged
        "a%2Fb".decodeURI() shouldBe "a/b"
        "%28%3F%29".decodeURI() shouldBe "(?)"
    }

    @Test fun `should decode URI string containing plus for space`() {
        "a+b".decodeURI() shouldBe "a b"
        "a+b+c%2Cd".decodeURI() shouldBe "a b c,d"
    }

    @Test fun `should determine whether character is unreserved for URI`() {
        'A'.isUnreservedForURI() shouldBe true
        'B'.isUnreservedForURI() shouldBe true
        'Y'.isUnreservedForURI() shouldBe true
        'Z'.isUnreservedForURI() shouldBe true
        'a'.isUnreservedForURI() shouldBe true
        'b'.isUnreservedForURI() shouldBe true
        'y'.isUnreservedForURI() shouldBe true
        'z'.isUnreservedForURI() shouldBe true
        '0'.isUnreservedForURI() shouldBe true
        '1'.isUnreservedForURI() shouldBe true
        '8'.isUnreservedForURI() shouldBe true
        '9'.isUnreservedForURI() shouldBe true
        '-'.isUnreservedForURI() shouldBe true
        '.'.isUnreservedForURI() shouldBe true
        '_'.isUnreservedForURI() shouldBe true
        '~'.isUnreservedForURI() shouldBe true
        '!'.isUnreservedForURI() shouldBe false
        '"'.isUnreservedForURI() shouldBe false
        '#'.isUnreservedForURI() shouldBe false
        '$'.isUnreservedForURI() shouldBe false
        '%'.isUnreservedForURI() shouldBe false
        '&'.isUnreservedForURI() shouldBe false
        '\''.isUnreservedForURI() shouldBe false
        '('.isUnreservedForURI() shouldBe false
        ')'.isUnreservedForURI() shouldBe false
        '*'.isUnreservedForURI() shouldBe false
        '+'.isUnreservedForURI() shouldBe false
        ','.isUnreservedForURI() shouldBe false
        '/'.isUnreservedForURI() shouldBe false
        ':'.isUnreservedForURI() shouldBe false
        ';'.isUnreservedForURI() shouldBe false
        '<'.isUnreservedForURI() shouldBe false
        '='.isUnreservedForURI() shouldBe false
        '>'.isUnreservedForURI() shouldBe false
        '?'.isUnreservedForURI() shouldBe false
        '@'.isUnreservedForURI() shouldBe false
        '['.isUnreservedForURI() shouldBe false
        '\\'.isUnreservedForURI() shouldBe false
        ']'.isUnreservedForURI() shouldBe false
        '^'.isUnreservedForURI() shouldBe false
        '`'.isUnreservedForURI() shouldBe false
        '{'.isUnreservedForURI() shouldBe false
        '|'.isUnreservedForURI() shouldBe false
        '}'.isUnreservedForURI() shouldBe false
    }

}
