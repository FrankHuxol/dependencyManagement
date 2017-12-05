package de.ebp.dependencymanagement.util;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;

import com.google.common.base.Optional;

public class Util {

	/**
	 * Converts the provided dependency object to a readable string:
	 * <p>
	 * group:artifact:packaging:[classifier:]version:scope
	 * 
	 * @param aDependency
	 * @return
	 */
	public static String toReadableString(Dependency aDependency) {
		Optional<String> groupId = Optional.fromNullable(aDependency.getGroupId());
		Optional<String> artifactId = Optional.fromNullable(aDependency.getArtifactId());
		Optional<String> packaging = Optional.fromNullable(aDependency.getType());
		Optional<String> classifier = Optional.fromNullable(aDependency.getClassifier());
		Optional<String> version = Optional.fromNullable(aDependency.getVersion());
		Optional<String> scope = Optional.fromNullable(aDependency.getScope());

		if (classifier.isPresent()) {
			return groupId.or("") + ":" + artifactId.or("") + ":" + packaging.or(new Dependency().getType()) + ":"
					+ classifier.or("") + ":" + version.or("") + ":" + scope.or(Artifact.SCOPE_COMPILE);
		} else {
			return groupId.or("") + ":" + artifactId.or("") + ":" + packaging.or(new Dependency().getType()) + ":"
					+ version.or("") + ":" + scope.or(Artifact.SCOPE_COMPILE);
		}

	}

}
