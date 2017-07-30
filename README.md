# MarkLogic XQuery Plugin

[![Build Status](https://travis-ci.org/rhdunn/marklogic-intellij-plugin.svg)](https://travis-ci.org/rhdunn/marklogic-intellij-plugin)

- [License Information](#license-information)
- [Features](#features)

----------

This project provides MarkLogic integration for the IntelliJ IDE.

_Supported IntelliJ Platforms:_ IntelliJ IDEA Community, IntelliJ IDEA Ultimate,
PhpStorm, WebStorm, PyCharm, RubyMine, AppCode, CLion, Rider, Android Studio

_Supported IntelliJ Versions:_ 2017.1 - 2017.2

_Supported MarkLogic Versions:_ 5.0 - 9.0

## Features

Supports running and profiling the following query types:
1. XQuery
1. Server-side JavaScript (MarkLogic 8 or later)
1. SQL (MarkLogic 8 or later, run only)
1. SPARQL Query (MarkLogic 7 or later, run only)
1. SPARQL Update (MarkLogic 8 or later, run only)

RDF triples can be returned in the following formats:
1. N3 (Notation3)
1. N-Quads
1. N-Triples
1. RDF/JSON
1. RDF/XML
1. TriG
1. Triples/XML
1. Turtle

__NOTE:__ On MarkLogic 7, these triple formats are reported as text/plain
due to limitations in the MarkLogic API.

## License Information

Copyright (C) 2017 Reece H. Dunn

This IntelliJ Plugin is licensed under the [Apache 2.0](LICENSE)
license.
