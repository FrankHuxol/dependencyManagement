# dependencyManagement
A maven plugin handling operations on the dependencymanagement section.

## Available goals

* enforce: Ensures, that the project is only using dependencies defined in the dependency management section.


## Build status
[![CircleCI](https://circleci.com/gh/FrankHuxol/dependencyManagement.svg?style=svg)](https://circleci.com/gh/FrankHuxol/dependencyManagement)

## Getting started

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

Run goal:
```
mvn dependencymanagement:enforce
```

That prints something like:
```
[DEBUG] Artifacts contained in dependency management
[DEBUG] + batik:batik-all:jar:1.7
[DEBUG] + cglib:cglib-nodep:jar:2.2.2:test
[DEBUG] + com.anotherbigidea:javaswf:jar:20021114
[DEBUG] + com.enterprisedt:edtftpj:jar:2.1.0
[WARNING] Artifact not in dependency management used: stax:stax-api:jar:1.0.1:compile (used by [MavenProject: ...])
[WARNING] Artifact not in dependency management used: org.orbisgis:poly2tri-core:jar:0.1.2:compile (used by [MavenProject: ..., MavenProject: ...])
```


## Authors
* Frank Huxol (https://github.com/FrankHuxol): Created git hub project - https://github.com/FrankHuxol/dependencyManagement
* Tunaki (https://stackoverflow.com/users/1743880/tunaki): Shared his solution - https://stackoverflow.com/questions/35630476/view-dependency-tree-of-dependencymanagement
