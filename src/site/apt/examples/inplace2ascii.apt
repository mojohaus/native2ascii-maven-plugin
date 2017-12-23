 ------
 Converting Resources Inplace
 ------
 David Matejcek
 ------
 2017-12-23
 ------

Converting UTF-8 properties to standard inplace

  This goal targets the scenario known from 1-beta version - conversion of
  UTF-8 file content to Unicode-escaped content on the same file.

  Note that this target is not covered by eclipse lifecycle.

---
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>native2ascii-maven-plugin</artifactId>
  <version>____</version>
  <executions>
    <execution>
      <id>utf8-to-latin1</id>
      <goals>
        <goal>inplace</goal>
      </goals>
      <phase>process-resources</phase><!-- default -->
      <configuration>
        <dir>${project.build.testOutputDirectory}/localized</dir><!-- must be set -->
        <encoding>${project.build.sourceEncoding}</encoding><!-- default -->
        <includes>
          <include>**/*.properties</include><!-- default -->
        </includes>
        <excludes>
          <exclude></exclude><!-- nothing by default -->
        </excludes>
      </configuration>
    </execution>
  </executions>
</plugin>
---
