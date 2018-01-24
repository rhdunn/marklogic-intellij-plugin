# MarkLogic XQuery Plugin

[![Build Status](https://travis-ci.org/rhdunn/marklogic-intellij-plugin.svg)](https://travis-ci.org/rhdunn/marklogic-intellij-plugin)

- [Features](#features)
  - [Run/Debug Configurations](#run-debug-configurations)
  - [Log Viewer](#log-viewer)
- [License Information](#license-information)

----------

This project provides MarkLogic integration for the IntelliJ IDE.

_Supported IntelliJ Platforms:_ IntelliJ IDEA Community, IntelliJ IDEA Ultimate,
PhpStorm, WebStorm, PyCharm, RubyMine, AppCode, CLion, Rider, Android Studio

_Supported IntelliJ Versions:_ 2017.1 - 2018.1

_Supported MarkLogic Versions:_ 5.0 - 9.0

## Features

### Run/Debug Configurations

Supports running and profiling the following query types, with the minimum
required MarkLogic version:

| Query Type             | Run | Profile |
|------------------------|-----|---------|
| XQuery                 | Yes | Yes     |
| Server-side JavaScript | 8.0 | 8.0     |
| SPARQL Query           | 7.0 | No      |
| SPARQL Update          | 8.0 | No      |
| SQL                    | 8.0 | No      |

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

### Log Viewer

Supports displaying MarkLogic `ErrorLog.txt` logs for the configured MarkLogic
instances.

## License Information

Copyright (C) 2017 Reece H. Dunn

This IntelliJ Plugin is licensed under the [Apache 2.0](LICENSE)
license.
