package de.ebp.dependencymanagement.tree;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.ebp.dependencymanagement.dependency.DependencyComparator;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.ProjectArtifact;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.aether.util.repository.DefaultProxySelector;

import java.util.*;

public class DependenciesTree {

    private final MavenProject project;
    private final Log log;
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession repositorySystemSession;

    public DependenciesTree(MavenProject aProject, Log mavenLog) {
        super();
        project = aProject;
        log = mavenLog;
        repositorySystem = newRepositorySystem(MavenRepositorySystemUtils.newServiceLocator());
        repositorySystemSession = newSession(repositorySystem);
    }

    public DependencyNode asDependencyNode(ResolutionOptions theResolutionOptions) {
        DependencyNode projectNode = createProjectNode();
        Set<Dependency> alreadyVisitedDependencies = new TreeSet<>(new DependencyComparator());
        List<DependencyNode> allDependencies = resolveDependenciesInDependencyManagement(projectNode, alreadyVisitedDependencies, theResolutionOptions);
        ((DefaultDependencyNode) projectNode).setChildren(Collections.unmodifiableList(allDependencies));
        return projectNode;
    }

    private DependencyNode createProjectNode() {
        ProjectArtifact projectArtifact = new ProjectArtifact(project);
        return createNode(null, projectArtifact);
    }

    private List<DependencyNode> resolveDependenciesInDependencyManagement(DependencyNode parent, Set<Dependency> alreadyVisitedDependencies, ResolutionOptions theResolutionOptions) {
        if (theResolutionOptions.getMaxDepth() == 0) {
            return new ArrayList<>();
        }
        int currentDependencyNumber = 1;
        List<DependencyNode> resolvedDependencies = new ArrayList<>();
        List<Dependency> dependenciesInDependencyManagement = project.getDependencyManagement().getDependencies();
        for (Dependency currentDependency : dependenciesInDependencyManagement) {
            log.info("Gathering dependency tree for " + currentDependency + " (" + currentDependencyNumber + "/" + dependenciesInDependencyManagement.size() + ")");
            alreadyVisitedDependencies.add(currentDependency);
            List<Exclusion> currentExclusions = currentDependency.getExclusions();
            if (isValidDirectDependency(currentDependency, theResolutionOptions)) {
                resolvedDependencies.add(resolveDependencies(parent, currentDependency, alreadyVisitedDependencies, theResolutionOptions.withReducedMaxDepth()));
            }

            currentDependencyNumber++;
        }
        return resolvedDependencies;
    }

    private List<DependencyNode> resolveDependencies(DependencyNode parent, List<Dependency> dependencies, Set<Dependency> alreadyVisitedDependencies, ResolutionOptions theResolutionOptions) {
        if (theResolutionOptions.getMaxDepth() == 0) {
            return new ArrayList<>();
        }
        List<DependencyNode> resolvedDependencies = new ArrayList<>();
        for (Dependency currentDependency : dependencies) {
            if (!isValidTransitiveDependency(currentDependency, theResolutionOptions)) {
                continue;
            }
            List<Exclusion> currentExclusions = currentDependency.getExclusions();
            boolean hasBeenAdded = alreadyVisitedDependencies.add(currentDependency);
            if (!theResolutionOptions.skipDuplicates() || hasBeenAdded) {
                resolvedDependencies.add(resolveDependencies(parent, currentDependency, alreadyVisitedDependencies, theResolutionOptions.withReducedMaxDepth()));
            } else {
                resolvedDependencies.add(createSkippedNode(parent, currentDependency, alreadyVisitedDependencies));
            }

        }
        return resolvedDependencies;
    }

    private DependencyNode createSkippedNode(DependencyNode theParent, Dependency aDependency, Set<Dependency> visitedDependencies) {
        DependencyNode dependencyNode = createNode(theParent, toArtifact(aDependency));
        if (getDependencies(toArtifact(aDependency)).isEmpty()) {
            // this dependency does not have further dependencies
            return dependencyNode;
        }
        // if this dependency has further dependencies, we skip them
        DefaultArtifact skippedArtifact = new DefaultArtifact("skipped already printed dependencies", "", "-", "", "", "", null);
        DefaultDependencyNode skippedNode = new DefaultDependencyNode(dependencyNode, skippedArtifact, null, null, null);
        skippedNode.setChildren(new ArrayList<>());
        ((DefaultDependencyNode) dependencyNode).setChildren(Lists.newArrayList(skippedNode));
        return dependencyNode;
    }


    private DependencyNode resolveDependencies(DependencyNode parent, Dependency aDependency, Set<Dependency> alreadyVisitedDependencies, ResolutionOptions theResolutionOptions) {
        Artifact artifact = toArtifact(aDependency);
        if (theResolutionOptions.getMaxDepth() == 0) {
            DefaultDependencyNode projectNode = new DefaultDependencyNode(parent, artifact, null, null, null);
            projectNode.setChildren(Collections.unmodifiableList(new ArrayList<>()));
            return projectNode;
        }
        List<Dependency> dependencies = getDependencies(aDependency);
        DependencyNode dependencyNode = createNode(parent, artifact);
        ((DefaultDependencyNode) dependencyNode).setChildren(resolveDependencies(dependencyNode, dependencies, alreadyVisitedDependencies, theResolutionOptions));
        return dependencyNode;
    }

    private List<Dependency> getDependencies(Dependency aDependency) {
        return getDependencies(toArtifact(aDependency));
    }

    private boolean isValidDirectDependency(Dependency aDependency, ResolutionOptions theResolutionOptions) {
        return scopeMatches(aDependency, theResolutionOptions);
    }

    private boolean scopeMatches(Dependency aDependency, ResolutionOptions theResolutionOptions) {
        if (theResolutionOptions.getScopes().isEmpty()) {
            return true;
        }
        return theResolutionOptions.getScopes().contains(Optional.ofNullable(aDependency.getScope()).orElse("compile"));
    }

    private boolean isValidTransitiveDependency(Dependency aDependency, ResolutionOptions theResolutionOptions) {
        return !isTestDependency(aDependency) && scopeMatches(aDependency, theResolutionOptions);
    }

    private boolean isTestDependency(Dependency aDependency) {
        return Optional.ofNullable(aDependency.getScope()).orElse("compile").equalsIgnoreCase("test");
    }

    private Artifact toArtifact(Dependency aDependency) {
        Optional<String> groupId = Optional.ofNullable(aDependency.getGroupId());
        Optional<String> artifactId = Optional.ofNullable(aDependency.getArtifactId());
        Optional<String> type = Optional.ofNullable(aDependency.getType());
        Optional<String> classifier = Optional.ofNullable(aDependency.getClassifier());
        Optional<String> version = Optional.ofNullable(aDependency.getVersion());
        Optional<String> scope = Optional.ofNullable(aDependency.getScope());
        return new DefaultArtifact(groupId.orElse(""), artifactId.orElse(""), version.orElse(""), scope.orElse(Artifact.SCOPE_COMPILE),
                type.orElse(new Dependency().getType()), classifier.orElse(""), null);
    }

    private List<Dependency> getDependencies(Artifact anArtifact) {
        try {
            org.eclipse.aether.artifact.Artifact artifact = convert(anArtifact);
            ArtifactDescriptorRequest request = new ArtifactDescriptorRequest(artifact, convert(project.getRemoteArtifactRepositories()), null);
            ArtifactDescriptorResult result = repositorySystem.readArtifactDescriptor(repositorySystemSession, request);

            List<Dependency> dependencies = new ArrayList<>();
            for (org.eclipse.aether.graph.Dependency dependency : result.getDependencies()) {
                dependencies.add(convert(dependency));
            }

            return dependencies;
        } catch (ArtifactDescriptorException | IllegalArgumentException e) {
            log.error("Could not retrieve dependencies for artifact " + anArtifact, e);
        }
        return new ArrayList<>();
    }


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
        return new org.eclipse.aether.artifact.DefaultArtifact(artifactCoords);
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
        return new RemoteRepository.Builder(mavenRepository.getId(), mavenRepository.getLayout().getId(), mavenRepository.getUrl()).build();
    }

    /**
     * Creates new repository system.
     *
     * @param locator The locator to use.
     * @return The repository system
     */
    private static RepositorySystem newRepositorySystem(DefaultServiceLocator locator) {
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }

    /**
     * Starts a new session for the provided repository.
     *
     * @param system The repository to start the session for.
     * @return The session
     */
    private static RepositorySystemSession newSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository("target/local-repo");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        // set possible proxies and mirrors
        session.setProxySelector(new DefaultProxySelector().add(new Proxy(Proxy.TYPE_HTTP, "host", 3625), "localhost|127.0.0.1"));
        session.setMirrorSelector(new DefaultMirrorSelector().add("my-mirror", "http://mirror", "default", false, "external:*", null));
        return session;
    }

    DependencyNode createNode(DependencyNode parent, Artifact artifact) {
        DefaultDependencyNode projectNode = new DefaultDependencyNode(parent, artifact, null, null, null);
        projectNode.setChildren(Collections.unmodifiableList(new ArrayList<>()));
        return projectNode;
    }
}
