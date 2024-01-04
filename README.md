# string-mapper

[![Build Status](https://travis-ci.com/pwall567/string-mapper.svg?branch=main)](https://app.travis-ci.com/github/pwall567/string-mapper)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v1.8.22&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v1.8.22)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.text/string-mapper?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.text%22%20AND%20a:%22string-mapper%22)

String mapping utilities

## Background

There are many cases where a string must be converted to or from an encoded or "escaped" form.
For example, in XML or HTML the characters "&lt;", "&amp;" and others must be encoded as multi-character strings
("&amp;lt;", "&amp;amp;" _etc._) to avoid being interpreted as part of structural or layout sequences.

For long sequences of data, a streaming approach may be preferred, and the
[`pipelines`](https://github.com/pwall567/pipelines) library exists to address this use case.
When converting short strings, the ideal technique is to create a new string only when needed, and if the input has no
characters that need replacing, return the original string unchanged (thus avoiding object allocation).

This library includes general-purpose encoding and decoding functions optimised for the case where many strings will not
require any mapping at all (for example, because they are relatively short), along with implementations for:

- URI percent-encoding and decoding
- UTF-8 encoding and decoding
- JSON string encoding and decoding

## Reference Guide

### `StringMapper`

The `StringMapper` object contains functions used by the specific encoding and decoding objects, but the functions may
be used independently.

#### `mapCharacters`

The `mapCharacters` function is an extension function on `String`, and it is used to map single characters to
multi-character sequences.
It takes a single parameter, a mapping function (usually expressed as a lambda following the function name).

The mapping function maps a character to its replacement string, or `null` to indicate that no mapping is required.
If all characters in the string map to `null` the original string will be returned unmodified; otherwise a new string
will be returned with the replacement strings substituted for the mapped characters.

For example, many systems that use double quotes to delimit strings require any occurrences of double quotes within the
string to be duplicated.
This can be achieved as follows:
```kotlin
    val newString = oldString.mapCharacters { if (it == '"') "\"\"" else null }
```

The result of the mapping function is actually a `CharSequence`; this means that if the function creates a result string
dynamically using a `StringBuilder`, there is no need to call `toString()` on the return value, saving an unnecessary
object creation.

#### `mapSubstrings`

The `mapSubstrings` function is used to decode substrings from an encoded string back to the original characters, and it
too is an extension function on `String`.
Like `mapCharacters`, it takes a mapping function as a parameter, but in this case the function is more complex.

The parameter to the mapping function is the index into the string to be examined (the function is expected to already
have access to the original string), and the return value is an object implementing the [`MapResult`](#mapresult)
interface; this supplies both the replacement (usually, but not necessarily, a single character) and the number of input
characters to be skipped.
For example, when decoding an XML string, the result of matching the characters "`&amp;lt;`" would be a `MapResult` with
a length of 4, substituting the character "`<`".

As with `mapCharacters`, a return value of `null` indicates that no mapping is required at the given index, and if all
calls to the mapping function result in `null` the original string will be returned.

Taking the earlier example of a string in which all occurrences of double quotes have been duplicated, the decoding of
the string would be something like:
```kotlin
    val original = encoded.mapSubstrings {
        if (encoded[it] == '"') {
            StringMapper.checkLength(encoded, it, 2) // check that 2 characters are available
            require(encoded[it + 1] == '"')
            CharMapResult(2, '"')
        } else null
    }
```

#### `checkLength`

This is a function used within a `mapSubstrings` mapping function to check that there are sufficient characters in the
string to complete the sequence.
The example above shows the use of `checkString` to confirm that the string has at least 2 remaining characters
(including the current character) at the position indicated by the index.

#### `buildResult`

This function assists with the creation of a dynamic mapping result.
It is principally intended for internal use; anyone wishing to make use of it will find examples in the implementing
functions described below.

### `JSONStringMapper`

`JSONStringMapper` provides functions to encode JSON string values using the backslash sequences defined in the
[JSON Standard](https://www.rfc-editor.org/rfc/rfc8259.html#section-7), and to decode strings encoded in this way.

To encode a string:
```kotlin
    val encoded = unencoded.encodeJSON()
```

And to decode a string:
```kotlin
    val decoded = encoded.decodeJSON()
```

### `URIStringMapper`

`URIStringMapper` provides functions to encode URI components using the percent-encoding process defined in the
[URI Syntax Standard](https://www.rfc-editor.org/rfc/rfc3986#section-2), and to decode strings encoded in this manner.
The encoding function will percent-encode all characters other than those defined in the standard as "unreserved", along
with the dollar sign "`$`" &ndash; this is passed through unencoded because of its use in fragment identifiers in such
standards as OpenAPI and JSON Schema.

This implementation does **not** use the plus sign "`+`" to encode a space character, but it will decode such usages.

To encode a string:
```kotlin
    val encoded = unencoded.encodeURI()
```

And to decode a string:
```kotlin
    val decoded = encoded.decodeURI()
```

To test whether a character is in the unreserved set:
```kotlin
    val isUnreserved: Boolean = ch.isUnreservedForURI()
```

### `UTF8StringMapper`

`UTF8StringMapper` provides functions to encode and decode strings to and from UTF-8 encoding.

To encode a string:
```kotlin
    val encoded = unencoded.encodeUTF8()
```

And to decode a string:
```kotlin
    val decoded = encoded.decodeUTF8()
```

### `MapResult`

`MapResult` is an interface to define the return value from the [`mapSubstrings`](#mapsubstrings) function.
It defines a single value `length` &ndash; the number of characters to be skipped in the input &ndash; and a function
`appendResult` to append the mapping result to a supplied `Appendable`.

There are three implementing classes provided: `CharMapResult`, which substitutes a single character, `StringMapResult`,
which substitutes a string, and `ElisionMapResult` which substitutes nothing.

## Dependency Specification

The latest version of the library is 2.2, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.text</groupId>
      <artifactId>string-mapper</artifactId>
      <version>2.2</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.text:string-mapper:2.2'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.text:string-mapper:2.2")
```

Peter Wall

2023-12-02
