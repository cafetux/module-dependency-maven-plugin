[![Coverage](.github/badges/jacoco.svg)](https://github.com/cafetux/module-dependency-maven-plugin/actions/workflows/maven.yml)

# maven module interdependencies analyzer

Sometimes, on Legacy projects, it's difficult to known which maven module is calling which maven module.

this maven plugin generate a DOT diagram file.

You can visualize them with DOT intellij plugin.

Generated file is named module-dependency.gv

## Usage

Plugin is available on maven central.

```xml
           <plugin>
                <groupId>io.github.cafetux</groupId>
                <artifactId>maven-modules-analyzer</artifactId>
                <version>2.1</version>
                <configuration/>
                <executions>
                    <execution>
                        <goals>
                            <goal>maven-analyzer</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

### Configuration
You can use some configuration on plugin:

```xml
   <configuration>
     <resultDirectory>path to generate results (dot diagram and image). 'target' by default</resultDirectory>
    <excludeScopes>
        <param>test</param>
        <param>another scope of dependencies you want to exclude</param>
    </excludeScopes>
    <excludeClassifiers>
        <param>test</param>
        <param>another classifier for dependencies you want to exclude</param>
    </excludeClassifiers>
    <excludeArtifactIds>
        <param>acceptance-test</param>
        <param>another artifact id you want to exclude from schema</param>
    </excludeArtifactIds>
    <includeExternalDependencies>true if you want to generate illisible diagram with external dependencies (like spring ect). false by default</includeExternalDependencies>
</configuration>
```

you can use as properties by prefixing configuration keys by dependency.graph.

## Complementary plugin
You can use another plugin to render .gv at png: https://github.com/cafetux/dot-diagram-renderer-maven-plugin
