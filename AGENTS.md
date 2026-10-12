# Agent guide for native2ascii-maven-plugin

This file is for automated agents and human contributors working on this repository. It records
what the code and the POMs do not make obvious.

## Layout

This is a single-module Maven plugin. The goals are `resources` (`Native2AsciiMojo`),
`testResources` (`Native2AsciiTestMojo`) and `inplace` (`Native2AsciiInplaceMojo`), all bound to
the resource phases and all extending `AbstractNative2AsciiMojo`. The conversion itself is in
`Native2Ascii` (file and directory conversion) and `PropertyEscaper` (the escaping).

- The site is built from APT pages in `src/site/apt`.
- The project is MIT licensed, unlike most of MojoHaus. Keep each file's existing header.

## Build gates

- `spotless-maven-plugin` from mojo-parent runs during `process-sources`. Run `mvn spotless:apply`
  before committing. It formats Java with palantir-java-format, sorts the POM, and also reformats
  Markdown, including this file and the README.
- The plugin compiles against `mavenVersion` (3.9.16) and declares a literal Maven 3.9.0 as its
  `<prerequisites>`; `minimalMavenBuildVersion` holds the build to the same floor, overriding
  the value mojo-parent 97 derives from `mavenVersion`. Raising `mavenVersion` must not raise the
  prerequisite.
- Fields of the mojo classes are plugin parameters; renaming one changes the plugin's
  configuration API.

## Unit tests

- JUnit 5 only, under `src/test/java`. `Native2AsciiMojoTest` creates the mojo with `new` and sets
  its fields directly; the input files are in `src/test/resources`.

## Integration tests

- They live in `src/it`, one directory per test, and there is no `run-its` profile: the invoker
  plugin is configured in the main build, so a plain `mvn verify` runs them after the unit tests.
  The invoker `install` goal puts the plugin into `target/local-repo` first. Run a subset with:

  ```shell
  mvn -B verify -Dinvoker.test=typical_usage,no_classes
  ```

  There are no `setup-*` projects. `no_classes` has no `verify.groovy`; it only has to build.

- They run in parallel, one per CPU core (`invoker.parallelThreads` 1C), and every forked Maven
  gets `JAVA_TOOL_OPTIONS=-XX:TieredStopAtLevel=1 -XX:+UseSerialGC`. A test must not depend on
  another test's output or on running alone.

- The projects run `clean install`, then `verify.groovy`. Write it as `assert` statements.
  Invoker fails a script whose last expression is anything other than `true` or `null`, so a
  trailing `list.each { }` fails the test; use a `for` loop.

- `default_encoding` reads `build.log` and the `mavenVersion` variable: Maven 3.10 sets
  `project.build.sourceEncoding` to UTF-8 by default, so the platform-encoding warning is expected
  on 3.9 and absent on 3.10. Keep that branch when you edit the script.

- To run the tests with another Maven, pass `-Dinvoker.mavenHome=<dir>`.

## Continuous integration

- `.github/workflows/maven.yml` calls the shared `maven-verify.yml@v5` workflow from
  `apache/maven-gh-actions-shared`, with `verify` as the build goal. It does not pass
  `-P run-its`, because Maven 4 fails on a requested profile that does not exist.
- A separate `codeql-analysis.yml` workflow runs CodeQL on pull requests and weekly.
- Branches pushed to this repository build under the `push` event.

## Dependencies

- Dependabot updates are monthly and grouped. `maven-plugin-api` stays below 3.10, because the
  plugin is built for the Maven 3.9 line.

## Pull requests

- Squash, merge and rebase merges are all enabled. Release Drafter, through the shared
  `apache/maven-gh-actions-shared` workflow, builds the release notes from pull request titles
  and labels, so the title should describe the change for a user.

