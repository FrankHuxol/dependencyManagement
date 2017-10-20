package dependencymanagementlist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.transfer.ArtifactNotFoundException;

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

@Mojo(name = "list", requiresDependencyResolution = ResolutionScope.TEST)
public class ListDependencyManagementMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	@Parameter(defaultValue = "${session}", readonly = true, required = true)
	private MavenSession session;

	@Parameter(defaultValue = "false", readonly = true, required = false)
	private boolean excludeTransitive;

	@Component
	private ArtifactHandler artifactHandler;

	@Component
	private ProjectBuilder projectBuilder;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().debug("Start dependency analysis");
		Set<Artifact> artifactsInDependencyManagement = new HashSet<Artifact>();
		for (Dependency dependency : project.getDependencyManagement().getDependencies()) {
			collectDependencyManagement(toArtifact(dependency), artifactsInDependencyManagement);
		}
		getLog().info("Artifacts contained in dependency management");
		for (Artifact artifact : Ordering.natural().nullsFirst().immutableSortedCopy(artifactsInDependencyManagement)) {
			getLog().info("+ " + artifact);
		}

		Set<Artifact> usedDependencies = new HashSet<Artifact>();
		for (MavenProject project : session.getAllProjects()) {
			collectDependencies(project, usedDependencies);
		}

		SetView<Artifact> artifactsUsedButNotInDependencyManagement = Sets.difference(usedDependencies,
				artifactsInDependencyManagement);
		if (artifactsUsedButNotInDependencyManagement.isEmpty()) {
			getLog().info("Good for you, not artifacts detected, which are not part of the dependencyManagement");
		} else {
			getLog().info("Artifacts referenced, but not contained in dependency management");
			for (Artifact artifact : Ordering.natural().nullsFirst().immutableSortedCopy(artifactsUsedButNotInDependencyManagement)) {
				getLog().warn("+ " + artifact);
			}
		}
		getLog().debug("End dependency analysis");
	}

	private void collectDependencies(MavenProject theProject, Set<Artifact> theCollectedArtifacts) {
		for (Dependency dependency : theProject.getDependencies()) {
			Artifact artifact = toArtifact(dependency);
			if (theCollectedArtifacts.contains(artifact)) {
				// already seen that, skip rest
				continue;
			}
			theCollectedArtifacts.add(artifact);
		}
	}

	private void collectDependencyManagement(Artifact artifact, Set<Artifact> visitedArtifacts)
			throws MojoExecutionException {
		visitedArtifacts.add(artifact);
		if (excludeTransitive) {
			for (Dependency transitive : getTransitiveDependencies(artifact)) {
				Artifact transitiveArtifact = toArtifact(transitive);
				if (!visitedArtifacts.contains(transitiveArtifact)) {
					visitedArtifacts.add(transitiveArtifact);
					collectDependencyManagement(transitiveArtifact, visitedArtifacts);
				}
			}
		}
	}

	private List<Dependency> getTransitiveDependencies(Artifact artifact) throws MojoExecutionException {
		try {
			ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(
					session.getProjectBuildingRequest());
			buildingRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
			buildingRequest.setProject(null);
			MavenProject mavenProject = projectBuilder.build(artifact, buildingRequest).getProject();
			return mavenProject.getDependencies();
		} catch (ProjectBuildingException e) {
			if (e.getCause() != null && e.getCause().getCause() instanceof ArtifactNotFoundException) {
				// ignore
				return new ArrayList<Dependency>();
			}
			throw new MojoExecutionException("Error while building project", e);
		}
	}

	private Artifact toArtifact(Dependency dependency) {
		return new DefaultArtifact(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(),
				dependency.getScope(), dependency.getType(), dependency.getClassifier(), artifactHandler);
	}

}
