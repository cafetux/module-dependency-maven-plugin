# maven module interdependencies analyzer

Sometimes, on Legacy projects, it's difficult to known which maven module is calling which maven module.

this maven plugin generate a DOT diagram file.

You can visualize them with DOT intellij plugin.

Generated file is named module-dependency.gv

Not currently available on maven central.

## Usage

If you have built this plugin in local, add on pom in build section:

```xml
           <plugin>
                <groupId>com.livido</groupId>
                <artifactId>maven-modules-analyzer</artifactId>
                <version>${version}</version>
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
     <renderImage>false to disable image generation. true by default</renderImage>
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
</configuration>
```

you can use as properties by prefixing configuration keys by dependency.graph.

