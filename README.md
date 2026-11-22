# Native2ascii Maven Plugin

[![License: MIT](https://img.shields.io/github/license/mojohaus/native2ascii-maven-plugin)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.mojo/native2ascii-maven-plugin.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.codehaus.mojo/native2ascii-maven-plugin)
[![JDK Compatibility](https://github.com/mojohaus/native2ascii-maven-plugin/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/mojohaus/native2ascii-maven-plugin/actions/workflows/maven.yml?query=branch%3Amaster)
[![CodeQL Analysis](https://github.com/mojohaus/native2ascii-maven-plugin/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/mojohaus/native2ascii-maven-plugin/actions/workflows/codeql-analysis.yml)

The plugin converts files in any supported character encoding to ASCII with Unicode escapes, like Java's
`java.util.Properties` expects. It lets you keep property files in UTF-8 and ship them in the form the runtime needs.

It requires Maven 3.9.0 or later and Java 8 or later. The goals are `native2ascii:resources`, which converts
`src/main/native2ascii` into the output directory, `native2ascii:testResources` for `src/test/native2ascii`, and
`native2ascii:inplace`, which rewrites the files of one directory. For example:

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>native2ascii-maven-plugin</artifactId>
  <version><!-- latest release --></version>
  <executions>
    <execution>
      <goals>
        <goal>resources</goal>
      </goals>
      <configuration>
        <encoding>UTF-8</encoding>
      </configuration>
    </execution>
  </executions>
</plugin>
```

The usage is described on the [wiki](https://github.com/mojohaus/native2ascii-maven-plugin/wiki), and the goal
parameters in the Javadoc of the mojo classes. This plugin is not affected by
[JDK-8074431](https://bugs.openjdk.org/browse/JDK-8074431).

Conventions for contributors and agents are in [AGENTS.md](AGENTS.md).
