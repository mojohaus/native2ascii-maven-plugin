title: Introduction
author: David Matejcek <dmatej@seznam.cz>
date: 2017-12-23

# Native2Ascii Maven Plugin

This plugin provides solution for developers who maintain property files in UTF-8 encoding
but runtime needs ASCII and supports Unicode-escaped characters just like Java's
`java.util.Properties` class.

This plugin converts files in any supported character encoding to one with ASCII
and/or Unicode escapes.

The plugin requires Maven 3.9.0 or later and Java 8 or later.

## Goals Overview

* [native2ascii:resources](./resources-mojo.html) Converts the files in `src/main/native2ascii` and writes the result to the output directory, keeping the originals.
* [native2ascii:testResources](./testResources-mojo.html) Converts the files in `src/test/native2ascii` and writes the result to the test output directory.
* [native2ascii:inplace](./inplace-mojo.html) Converts the files of one directory in place.

See [Goals](./plugin-info.html) and the examples to see more:

* [Converting Resources](./examples/resources2ascii.html)
* [Converting Test Resources](./examples/test-resources2ascii.html)
* [Converting Resources Inplace](./examples/inplace2ascii.html)

