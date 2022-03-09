package de.ebp.dependencymanagement;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.ProjectArtifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.SerializingDependencyNodeVisitor;

import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;

import de.ebp.dependencymanagement.graph.FullDependenciesGraphBuilder;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mojo(name = "tree", requiresDependencyResolution = ResolutionScope.TEST)
public class TreeMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "depth", defaultValue = "3")
    private int depth;

    @Override
    public void execute() {
        DependencyNode projectNode = createDependenciesGraph();

        logDependencies(projectNode);
    }

    /**
     * Creates the dependencies graph.
     *
     * @return The full dependency graph
     */
    private DependencyNode createDependenciesGraph() {
        int maxResolutionDepth = 1;
        ProjectArtifact projectArtifact = new ProjectArtifact(project);
        FullDependenciesGraphBuilder graphBuilder = new FullDependenciesGraphBuilder(
                projectArtifact.getArtifactHandler());

        // create root node, but do not resolve anything
        DependencyNode projectNode = graphBuilder.createNode(projectArtifact, null, 0);

        // now iterate over dependencymanagement section and add all
        // dependencies from there
        List<DependencyNode> childNodes = new ArrayList<>();
        for (Dependency currentDependency : project.getDependencyManagement().getDependencies()) {
            childNodes.add(graphBuilder.createNode(currentDependency, projectNode, maxResolutionDepth));
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
