package de.ebp.dependencymanagement;

import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;
import de.ebp.dependencymanagement.dependency.DependencyComparator;
import de.ebp.dependencymanagement.graph.FullDependenciesGraphBuilder;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.ProjectArtifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.SerializingDependencyNodeVisitor;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Mojo(name = "tree", requiresDependencyResolution = ResolutionScope.TEST)
public class TreeMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "tree.maxDepth", defaultValue = "-1")
    private int maxDepth;

    @Parameter(property = "tree.scopes")
    private List<String> scopes;

    @Parameter(property = "tree.skipDuplicates", defaultValue = "false")
    private boolean skipDuplicates;

    @Override
    public void execute() {
        // The reason to first create the graph and then log it is to get the log output consecutive without interruptions due to downloading more artifacts
        getLog().info("Gathering dependency tree of dependencyManagement section. May take a while");
        DependencyNode projectNode = createDependenciesGraph();

        logDependencies(projectNode);
    }

    /**
     * Creates the dependencies graph.
     *
     * @return The full dependency graph
     */
    private DependencyNode createDependenciesGraph() {
        int maxResolutionDepth = maxDepth;
        ProjectArtifact projectArtifact = new ProjectArtifact(project);
        FullDependenciesGraphBuilder graphBuilder = new FullDependenciesGraphBuilder(project, getLog(), scopes, skipDuplicates);

        Set<Dependency> visitedDependencies = new TreeSet<>(new DependencyComparator());

        // create root node, but do not resolve anything
        DependencyNode projectNode = graphBuilder.createNode(projectArtifact, null, visitedDependencies, 0);

        // now iterate over dependencymanagement section and add all
        // dependencies from there
        List<DependencyNode> childNodes = new ArrayList<>();
        if (maxResolutionDepth != 0) {
            int currentDependencyNumber = 1;
            for (Dependency currentDependency : project.getDependencyManagement().getDependencies()) {
                getLog().info("Gathering dependency tree for " + currentDependency + " (" + currentDependencyNumber + "/" + project.getDependencyManagement().getDependencies().size() + ")");
                if (scopes.isEmpty() || scopes.contains(Optional.ofNullable(currentDependency.getScope()).orElse("compile"))) {
                    visitedDependencies.add(currentDependency);
                    childNodes.add(graphBuilder.createNode(currentDependency, projectNode, visitedDependencies, maxResolutionDepth - 1));
                }
                currentDependencyNumber++;
            }
        }
        ((DefaultDependencyNode) projectNode).setChildren(Collections.unmodifiableList(childNodes));
        return projectNode;
    }

    /**
     * Sends the provided node and its contained dependencies to log.info.
     *
     * @param theProjectNode The node, which should be logged
     */
    private void logDependencies(DependencyNode theProjectNode) {
        Writer writer = new StringWriter();
        SerializingDependencyNodeVisitor v = new SerializingDependencyNodeVisitor(writer,
                SerializingDependencyNodeVisitor.STANDARD_TOKENS);

        theProjectNode.accept(v);

        Iterable<String> singleLines = Splitter.on(Objects.requireNonNull(StandardSystemProperty.LINE_SEPARATOR.value())).omitEmptyStrings()
                .split(writer.toString());
        getLog().info("Dependency tree of dependencymanagement configuration:");
        for (String singleLine : singleLines) {
            getLog().info(singleLine);
        }
    }
}
