# Salty Engine Setup

Salty Engine Setup is a small command-line program written with Java that creates a runnable Salty Engine game project template.

Info: This software does not install the engine but rather requires a correct installation of it. See [here](https://github.com/edgelord314/salty-engine#how-to-use-salty-engine) for a tutorial on  how to install Salty Engine.


## How to use it
1. Download an existing release [here](https://github.com/edgelord314/salty-engine-setup/releases) or clone and build the project yourself using maven.
2. Start the jar with a CLI (command-line interface)
3. Parameterize the project as following
    - with program arguments: 
        ```bash
        -l LOCATION -n GAME-NAME -r WIDTHxHEIGHT -fps FPS -f FIXED_MILLIS-s true/false -g GROUP-ID -a ARTIFACT
        ```
        e.g.
        ```bash
        -l /Users/edgelord/workspace/myGame -n My Game -r 1920x1080 -fps 60 -f 5 -s false -g de.edgelord.myGame -a myGame-fps
        ```
    - without arguments, the program will prompt you for each parameter
    
You can now build and run the project using `maven`.

In both cases, the program will have created a project like that on success:

```
.
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── de
    │   │       └── edgelord
    │   │           └── myGame
    │   │               └── main
    │   │                   └── Main.java
    │   └── resources
    └── test
        └── java
```

The package name is created by replacing all "-" in the groupId with "."

The pom.xml looks like this:

```bash
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>{given groupId}</groupId>
    <artifactId>{given artifactId}</artifactId>
    <version>1.0-SNAPSHOT</version>

    <name>{given name}</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>de.edgelord.salty-engine</groupId>
            <artifactId>salty-engine</artifactId>
            <version>{given Salty Engine version}</version>
        </dependency>
    </dependencies>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <artifactId>maven-clean-plugin</artifactId>
                    <version>3.0.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-resources-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.7.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>2.20.1</version>
                </plugin>
                <plugin>
                    <artifactId>maven-jar-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-install-plugin</artifactId>
                    <version>2.5.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-deploy-plugin</artifactId>
                    <version>2.8.2</version>
                </plugin>
            </plugins>
        </pluginManagement>
        <plugins>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>{given groupId with all "-" replaced by "."}.main.Main</mainClass>
                        </manifest>
                    </archive>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <phase>package</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

The `Main.java` looks like this:

```bash
package de.edgelord.myGame.main;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.GameConfig;
import de.edgelord.saltyengine.displaymanager.display.SplashWindow;
import de.edgelord.saltyengine.scene.SceneManager;

public class Main extends Game {

    public static void main(String[] args) {
        init(GameConfig.config({given res width}, {given res height}, "{given game name}", {given fixed tick millis}));
        {parameterized start}
    }
}
```