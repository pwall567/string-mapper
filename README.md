# string-mapper

[![Build Status](https://travis-ci.com/pwall567/string-mapper.svg?branch=main)](https://app.travis-ci.com/github/pwall567/string-mapper)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v1.6.10&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v1.6.10)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.text/string-mapper?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.text%22%20AND%20a:%22string-mapper%22)

String mapping utilities

## Background

There are many cases where a string must be converted to or from an encoded or "escaped" form.
For example, in XML or HTML the characters "&lt;", "&amp;" and others must be encoded as multi-character strings
("&amp;lt;", "&amp;amp;" _etc._) to avoid being interpreted as part of structural or layout sequences.

For long sequences of data, a pipeline approach may be preferred, and the
[`pipelines`](https://github.com/pwall567/pipelines) exists to address this use case.
For converting short strings, the ideal technique is to create a new string only when needed, and when the input has no
characters that need replacing, return the original string (thus avoiding object allocation).

This library includes optimised encoding and decoding functions, along with implementations for:

- URI percent-encoding
- UTF-8 encoding

More documentation to follow; in the meantime examples of the use of the functions may be found in the unit tests.

## Dependency Specification

The latest version of the library is 1.1, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.text</groupId>
      <artifactId>string-mapper</artifactId>
      <version>1.1</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.text:string-mapper:1.1'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.text:string-mapper:1.1")
```

Peter Wall

2022-10-16
