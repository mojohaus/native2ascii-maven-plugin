 ------
 Converting Resources
 ------
 David Matejcek
 ------
 2017-12-23
 ------

Converting UTF-8 properties to standard

  This goal targets the basic scenario - conversion of UTF-8 files to Unicode-escaped files
  without any change of the originals.

---
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>native2ascii-maven-plugin</artifactId>
  <version>____</version>
  <executions>
    <execution>
      <id>utf8-to-latin1</id>
      <goals>
        <goal>resources</goal>
      </goals>
      <phase>process-resources</phase><!-- default -->
      <configuration>
        <srcDir>src/main/native2ascii</srcDir><!-- default -->
        <targetDir>${project.build.outputDirectory}</targetDir><!-- default -->
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
