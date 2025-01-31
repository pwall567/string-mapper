# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [3.1] - 2025-01-31
### Changed
- `pom.xml`: updated dependency version

## [3.0] - 2025-01-26
### Changed
- `pom.xml`: moved to `io.kstuff` (package amd Maven group)
- `pom.xml`: updated Kotlin version to 2.0.21
- tests: converted to `should-test` library

## [2.3] - 2024-07-10
### Added
- `build.yml`, `deploy.yml`: converted project to GitHub Actions
### Changed
- `README.md`: fixed initial description of escaped characters
- `pom.xml`: upgraded Kotlin version to 1.9.24
### Removed
- `.travis.yml`

## [2.2] - 2023-12-02
### Changed
- `pom.xml`: updated dependency version

## [2.1] - 2023-11-10
### Changed
- `JSONStringMapper`: use lower case for hexadecimal \uxxxx constructs
- `pom.xml`: upgraded Kotlin version to 1.8.22
- `pom.xml`: updated dependency version

## [2.0] - 2022-10-23
### Added
- `JSONStringMapper`: JSON string encoding and decoding
### Changed
- `StringMapper`, `UTF8StringMapper`: renamed functions (for consistency)

## [1.1] - 2022-10-16
### Changed
- `StringMapper`, `URIStringMapper`, `UTF8StringMapper`: simplified API for `mapSubstring`

## [1.0] - 2022-10-16
### Added
- all files: initial versions
