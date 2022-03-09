package de.ebp.dependencymanagement.graph;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.model.Dependency;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
     * @param anArtifact            the Artifact to create a dependency node for
     * @param theParent             The parent to create the node for
     * @param theScope              The scope to create the node for
     * @param theMaxResolutionDepth The maximum resolution depth to use
     * @return A dependency node for the given parameters
     */
    public DependencyNode createNode(Artifact anArtifact, DependencyNode theParent, String theScope,
                                     int theMaxResolutionDepth) {
        DefaultDependencyNode artifactNode = new DefaultDependencyNode(theParent, anArtifact, null, theScope, null);
        if (theMaxResolutionDepth <= 0) {
            artifactNode.setChildren(Collections.unmodifiableList(new ArrayList<>()));
            return artifactNode;
        }

        List<DependencyNode> childNodes = new ArrayList<>();
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
     * @param anArtifact            The artifact to create the node for
     * @param theParent             The parent to create the node for
     * @param theMaxResolutionDepth The maximum resolution depth to use
     * @return A dependency node for the given parameters
     */
    public DependencyNode createNode(Artifact anArtifact, DependencyNode theParent, int theMaxResolutionDepth) {
        return createNode(anArtifact, theParent, null, theMaxResolutionDepth);
    }

    /**
     * Creates a new dependency node for the provided dependency using the
     * provided node as parent.
     *
     * @param aDependency           The dependency to create the node for
     * @param theParent             The parent to create the node for
     * @param theMaxResolutionDepth Specifies the max depth for resolution
     * @return A dependency node for the given parameters
     */
    public DependencyNode createNode(Dependency aDependency, DependencyNode theParent, int theMaxResolutionDepth) {
        return this.createNode(toArtifact(aDependency), theParent, theMaxResolutionDepth);
    }

    /**
     * Converts the provided dependency to an artifact.
     *
     * @param aDependency The dependency to resolve to an artifact
     * @return The artifact
     */
    private Artifact toArtifact(Dependency aDependency) {
        Optional<String> groupId = Optional.ofNullable(aDependency.getGroupId());
        Optional<String> artifactId = Optional.ofNullable(aDependency.getArtifactId());
        Optional<String> type = Optional.ofNullable(aDependency.getType());
        Optional<String> classifier = Optional.ofNullable(aDependency.getClassifier());
        Optional<String> version = Optional.ofNullable(aDependency.getVersion());
        Optional<String> scope = Optional.ofNullable(aDependency.getScope());
        return new DefaultArtifact(groupId.orElse(""), artifactId.orElse(""), version.orElse(""), scope.orElse(Artifact.SCOPE_COMPILE),
                type.orElse(new Dependency().getType()), classifier.orElse(""), artifactHandler);
    }

    /**
     * Retrieves the dependencies of the provided artifact.
     *
     * @param anArtifact Retrieves the depedencies of the given artifact
     * @return The dependencies of the provided artifact
     */
    private List<Dependency> getDependencies(Artifact anArtifact) {
        return new ArrayList<>();
    }

    /**
     * Converts the provided aether dependency to regular maven dependency
     * @param aetherDep The aether dependency to convert.
     * @return The converted maven dependency.
     */
    private Dependency convert(org.eclipse.aether.graph.Dependency aetherDep) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(aetherDep.getArtifact().getGroupId());
        dependency.setArtifactId(aetherDep.getArtifact().getArtifactId());
        dependency.setVersion(aetherDep.getArtifact().getVersion());
        dependency.setType(aetherDep.getArtifact().getExtension());
        dependency.setScope(aetherDep.getScope());
        return dependency;
    }

    /**
     * Converts the provided maven artifact to an aether artifact.
     *
     * @param mavenArtifact The maven artifact to convert.
     * @return The converted aether artifact.
     */
    private org.eclipse.aether.artifact.Artifact convert(Artifact mavenArtifact) {
        String artifactCoords = Joiner.on(':').join(mavenArtifact.getGroupId(), mavenArtifact.getArtifactId(), mavenArtifact.getVersion());
        org.eclipse.aether.artifact.Artifact artifact = new org.eclipse.aether.artifact.DefaultArtifact(artifactCoords);
        return artifact;
    }

    /**
     * Converts the provided maven repositories to aether repositories.
     *
     * @param mavenRepositories The maven repositories to convert.
     * @return The converted aether repositories.
     */
    private List<RemoteRepository> convert(List<ArtifactRepository> mavenRepositories) {
        List<RemoteRepository> aetherRepositories = new ArrayList<>();
        for (ArtifactRepository mavenRepository : mavenRepositories) {
            aetherRepositories.add(convert(mavenRepository));
        }
        return aetherRepositories;
    }

    /**
     * Converts the provided maven repository to an aether repository.
     *
     * @param mavenRepository The maven repository to convert.
     * @return The converted aether repository.
     */
    private RemoteRepository convert(ArtifactRepository mavenRepository) {
        return new RemoteRepository.Builder(mavenRepository.getId(), mavenRepository.getBasedir(), mavenRepository.getUrl()).build();
    }
}
