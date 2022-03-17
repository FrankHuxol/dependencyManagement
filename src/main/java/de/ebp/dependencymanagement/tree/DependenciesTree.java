package de.ebp.dependencymanagement.tree;

import de.ebp.dependencymanagement.dependency.DependencyComparator;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.ProjectArtifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;

import java.util.*;

public class DependenciesTree {

    private final MavenProject project;
    private final Log log;

    public DependenciesTree(MavenProject aProject, Log mavenLog) {
        super();
        project = aProject;
        log = mavenLog;
    }

    public DependencyNode asDependencyNode(int maxDepth) {
        // create root node, but do not resolve anything
        DependencyNode projectNode = createProjectNode();

        Set<Dependency> alreadyVisitedDependencies = new TreeSet<>(new DependencyComparator());
        List<DependencyNode> allDependencies = resolveDependenciesInDependencyManagement(alreadyVisitedDependencies, maxDepth);
        ((DefaultDependencyNode) projectNode).setChildren(Collections.unmodifiableList(allDependencies));
        return projectNode;
    }

    private DependencyNode createProjectNode() {
        ProjectArtifact projectArtifact = new ProjectArtifact(project);
        DefaultDependencyNode projectNode = new DefaultDependencyNode(null, projectArtifact, null, null, null);
        projectNode.setChildren(Collections.unmodifiableList(new ArrayList<>()));
        return projectNode;
    }

    private List<DependencyNode> resolveDependenciesInDependencyManagement(Set<Dependency> alreadyVisitedDependencies, int maxDepth) {
        if (maxDepth != 0) {
            return new ArrayList<>();
        }

        List<Dependency> dependencies = project.getDependencyManagement().getDependencies();
        return resolveDependencies(dependencies, true);
    }

    private List<DependencyNode> resolveDependencies(List<Dependency> dependencies, boolean doLog) {
        int currentDependencyNumber = 1;
        List<DependencyNode> childNodes = new ArrayList<>();
        for (Dependency currentDependency : dependencies) {
            if (doLog) {
                log.info("Gathering dependency tree for " + currentDependency + " (" + currentDependencyNumber + "/" + dependencies.size() + ")");
            }
            List<Exclusion> currentExclusions = currentDependency.getExclusions();
//            alreadyVisitedDependencies.add(currentDependency);
//            childNodes.add(convertToDependencyNode(currentDependency, alreadyVisitedDependencies, maxDepth - 1));

            currentDependencyNumber++;
        }
        return childNodes;
    }

/*    private DependencyNode createDependencyNode() {
        DefaultDependencyNode artifactNode = new DefaultDependencyNode(theParent, anArtifact, null, theScope, null);
        return artifactNode;
    }*/

}
