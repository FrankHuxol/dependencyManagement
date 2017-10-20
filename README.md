# dependencyManagement
A maven module scanning the dependency management and enforcing, that no dependencies other than those are used.

# Usage

Add to pom:
```
	<build>
		<plugins>
			<plugin>
			    <groupId>de.ebp</groupId>
			    <artifactId>dependencymanagement-maven-plugin</artifactId>
			    <version>1.0.0/version>
			</plugin>
		</plugins>
	</build>
```

Run goal
```
mvn dependencymanagement:enforce
```


# Authors
* Frank Huxol (https://github.com/FrankHuxol): Created git hub project - https://github.com/FrankHuxol/dependencyManagementListing
* Tunaki (https://stackoverflow.com/users/1743880/tunaki): Shared his solution - https://stackoverflow.com/questions/35630476/view-dependency-tree-of-dependencymanagement
