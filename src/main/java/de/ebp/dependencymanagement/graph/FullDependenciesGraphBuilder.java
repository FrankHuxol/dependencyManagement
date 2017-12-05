package de.ebp.dependencymanagement.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.model.Dependency;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;

import com.google.common.base.Optional;

public class FullDependenciesGraphBuilder {

	private final ArtifactHandler artifactHandler;

	public FullDependenciesGraphBuilder(ArtifactHandler anArtifactHandler) {
		super();
		this.artifactHandler = anArtifactHandler;
	}

	/**
	 * Creates a node for the provided artifact, using the provided node as
	 * parent.
	 * 
	 * @param anArtifact
	 * @param theParent
	 * @param theScope
	 * @param theMaxResolutionDepth
	 * @return
	 */
	public DependencyNode createNode(Artifact anArtifact, DependencyNode theParent, String theScope,
			int theMaxResolutionDepth) {
		DefaultDependencyNode artifactNode = new DefaultDependencyNode(theParent, anArtifact, null, theScope, null);
		if (theMaxResolutionDepth <= 0) {
			artifactNode.setChildren(Collections.unmodifiableList(new ArrayList<DependencyNode>()));
			return artifactNode;
		}

		List<DependencyNode> childNodes = new ArrayList<DependencyNode>();
		for (Dependency currentDependency : getDependencies(anArtifact)) {
			childNodes.add(createNode(toArtifact(currentDependency), artifactNode, theMaxResolutionDepth - 1));
		}
		artifactNode.setChildren(Collections.unmodifiableList(childNodes));
		return artifactNode;
	}

	/**
	 * Creates a new dependency node for the provided dependency using the
	 * provided node as parent.
	 * 
	 * @param anArtifact
	 * @param theParent
	 * @param theMaxResolutionDepth
	 * @return
	 */
	public DependencyNode createNode(Artifact anArtifact, DependencyNode theParent, int theMaxResolutionDepth) {
		return createNode(anArtifact, theParent, null, theMaxResolutionDepth);
	}

	/**
	 * Creates a new dependency node for the provided dependency using the
	 * provided node as parent.
	 * 
	 * @param aDependency
	 * @param theParent
	 * @param theMaxResolutionDepth
	 *            Specifies the max depth for resolution
	 * @return
	 */
	public DependencyNode createNode(Dependency aDependency, DependencyNode theParent, int theMaxResolutionDepth) {
		return this.createNode(toArtifact(aDependency), theParent, theMaxResolutionDepth);
	}

	/**
	 * Converts the provided dependency to an artifact.
	 * 
	 * @param aDependency
	 * @return
	 */
	private Artifact toArtifact(Dependency aDependency) {
		Optional<String> groupId = Optional.fromNullable(aDependency.getGroupId());
		Optional<String> artifactId = Optional.fromNullable(aDependency.getArtifactId());
		Optional<String> type = Optional.fromNullable(aDependency.getType());
		Optional<String> classifier = Optional.fromNullable(aDependency.getClassifier());
		Optional<String> version = Optional.fromNullable(aDependency.getVersion());
		Optional<String> scope = Optional.fromNullable(aDependency.getScope());
		return new DefaultArtifact(groupId.or(""), artifactId.or(""), version.or(""), scope.or(Artifact.SCOPE_COMPILE),
				type.or(new Dependency().getType()), classifier.or(""), artifactHandler);
	}

	/**
	 * Retrieves the dependencies of the provided artifact.
	 * 
	 * @param anArtifact
	 * @return
	 */
	private List<Dependency> getDependencies(Artifact anArtifact) {
		return new ArrayList<Dependency>();
	}

}
