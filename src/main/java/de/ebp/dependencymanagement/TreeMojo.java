package de.ebp.dependencymanagement;

import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;
import de.ebp.dependencymanagement.tree.DependenciesTree;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.SerializingDependencyNodeVisitor;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

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
        DependenciesTree tree = new DependenciesTree(project, getLog());

        return tree.asDependencyNode(maxDepth);
    }

    /**
     * Sends the provided node and its contained dependencies to log.info.
     *
     * @param theProjectNode The node, which should be logged
     */
    private void logDependencies(DependencyNode theProjectNode) {
        Iterable<String> singleLines = collectLogItems(theProjectNode);
        getLog().info("Dependency tree of dependencymanagement configuration:");
        for (String singleLine : singleLines) {
            getLog().info(singleLine);
        }
    }

    private Iterable<String> collectLogItems(DependencyNode theProjectNode) {
        Writer writer = new StringWriter();
        SerializingDependencyNodeVisitor visitor = new SerializingDependencyNodeVisitor(writer,
                SerializingDependencyNodeVisitor.STANDARD_TOKENS);
        theProjectNode.accept(visitor);
        return Splitter.on(Objects.requireNonNull(StandardSystemProperty.LINE_SEPARATOR.value())).omitEmptyStrings()
                .split(writer.toString());
    }
}
