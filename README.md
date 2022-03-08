# dependencyManagement
A maven plugin handling operations on the dependencymanagement section.

## Build status
[![FrankHuxol](https://circleci.com/gh/FrankHuxol/dependencyManagement.svg?style=svg)](https://circleci.com/gh/FrankHuxol/dependencyManagement) powered by CircleCI

# Available goals

## enforce
Ensures, that the project is only using dependencies defined in the dependency management section.
```
[DEBUG] Artifacts contained in dependency management
[DEBUG] + batik:batik-all:jar:1.7
[DEBUG] + cglib:cglib-nodep:jar:2.2.2:test
[DEBUG] + com.anotherbigidea:javaswf:jar:20021114
[DEBUG] + com.enterprisedt:edtftpj:jar:2.1.0
[WARNING] Artifact not in dependency management used: stax:stax-api:jar:1.0.1:compile (used by [MavenProject: ...])
[WARNING] Artifact not in dependency management used: org.orbisgis:poly2tri-core:jar:0.1.2:compile (used by [MavenProject: ..., MavenProject: ...])
```

## tree
```
[INFO] Dependency tree of dependencymanagement configuration:
[INFO] de.ebp:dependencymanagement-maven-plugin:maven-plugin:2.0.0-SNAPSHOT
[INFO] +- org.apache.maven:maven-compat:jar:3.5.2:test
[INFO] |  +- org.codehaus.plexus:plexus-interpolation:jar:1.24:compile
[INFO] |  \- org.apache.maven.wagon:wagon-provider-api:jar:2.12:test
```

# Getting started

Add to pom:
```
	<build>
		<plugins>
			<plugin>
			    <groupId>de.ebp</groupId>
			    <artifactId>dependencymanagement-maven-plugin</artifactId>
			    <version>1.0.0</version>
			</plugin>
		</plugins>
	</build>
```

Run one of the goals:
```
mvn dependencymanagement:enforce
mvn dependencymanagement:tree
```



# Authors
* Frank Huxol (https://github.com/FrankHuxol): Created git hub project - https://github.com/FrankHuxol/dependencyManagement
* Tunaki (https://stackoverflow.com/users/1743880/tunaki): Shared his solution - https://stackoverflow.com/questions/35630476/view-dependency-tree-of-dependencymanagement
