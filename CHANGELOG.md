# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased](https://elasticpath.visualstudio.com/_git/laputa?path=%2Ftesting&version=GBmaster)

### Fixed

- Print-failure should always print out banner and contexts. [#348](https://elasticpath.visualstudio.com/_git/laputa/pullrequest/348?_a=overview)
- The `catch-test` macro was not reporting any error in case of unexpected resolve. [#406]((https://elasticpath.visualstudio.com/_git/laputa/pullrequest/406?_a=overview)

### Added
- Macros for more readable promise testing: `then-test` and `catch-test`.
- Helper function for async testing: `test-async`, `test-promise`, `test-channel`.
- Report line that rejected the Promise from testing functions. [#328](https://elasticpath.visualstudio.com/_git/laputa/pullrequest/328?_a=overview)

### Changed
- Make sure we return the right thing from async testing functions. [#328](https://elasticpath.visualstudio.com/_git/laputa/pullrequest/328?_a=overview)
