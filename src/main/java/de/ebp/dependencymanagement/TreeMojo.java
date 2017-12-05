package de.ebp.dependencymanagement;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

@Mojo(name = "tree", requiresDependencyResolution = ResolutionScope.TEST)
public class TreeMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		DependencyNode projectNode = createDependenciesGraph();

		logDependencies(projectNode);
	}

	/**
	 * Creates the dependencies graph.
	 * 
	 * @return
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
		List<DependencyNode> childNodes = new ArrayList<DependencyNode>();
		for (Dependency currentDependency : project.getDependencyManagement().getDependencies()) {
			childNodes.add(graphBuilder.createNode(currentDependency, projectNode, maxResolutionDepth));
		}
		((DefaultDependencyNode) projectNode).setChildren(Collections.unmodifiableList(childNodes));
		return projectNode;
	}

	/**
	 * Sends the provided node and its contained dependencies to log.info.
	 * 
	 * @param theProjectNode
	 */
	private void logDependencies(DependencyNode theProjectNode) {
		Writer writer = new StringWriter();
		SerializingDependencyNodeVisitor v = new SerializingDependencyNodeVisitor(writer,
				SerializingDependencyNodeVisitor.STANDARD_TOKENS);

		theProjectNode.accept(v);

		List<String> singleLines = Splitter.on(StandardSystemProperty.LINE_SEPARATOR.value()).omitEmptyStrings()
				.splitToList(writer.toString());
		getLog().info("Dependency tree of dependencymanagement configuration:");
		for (String singleLine : singleLines) {
			getLog().info(singleLine);
		}
	}

}
