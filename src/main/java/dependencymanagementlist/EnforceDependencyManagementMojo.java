package dependencymanagementlist;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

@Mojo(name = "enforce", requiresDependencyResolution = ResolutionScope.TEST)
public class EnforceDependencyManagementMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	@Parameter(defaultValue = "${session}", readonly = true, required = true)
	private MavenSession session;

	@Component
	private ArtifactHandler artifactHandler;

	@Component
	private ProjectBuilder projectBuilder;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Set<Artifact> artifactsInDependencyManagement = getArtifactsInDependencyManagement();
		getLog().debug("Artifacts contained in dependency management");
		for (Artifact artifact : Ordering.natural().nullsFirst().immutableSortedCopy(artifactsInDependencyManagement)) {
			getLog().debug("+ " + artifact);
		}

		enforceOnlyArtifactsInDependencyManagementAreUsed(artifactsInDependencyManagement);
	}

	/**
	 * Iterates over all projects / modules and enforces, that only artifacts
	 * included in the provided set are used.
	 * 
	 * @param theAllowedArtifacts
	 */
	private void enforceOnlyArtifactsInDependencyManagementAreUsed(Set<Artifact> theAllowedArtifacts) {
		boolean allGood = true;
		for (MavenProject currentProject : session.getAllProjects()) {
			Set<Artifact> usedArtifacts = currentProject.getDependencies().stream().map(this::toArtifact)
					.collect(Collectors.toSet());
			SetView<Artifact> artifactsUsedButNotInDependencyManagement = Sets.difference(usedArtifacts,
					theAllowedArtifacts);
			if (!artifactsUsedButNotInDependencyManagement.isEmpty()) {
				getLog().info("Artifacts referenced, but not contained in dependency management");
				for (Artifact artifact : Ordering.natural().nullsFirst()
						.immutableSortedCopy(artifactsUsedButNotInDependencyManagement)) {
					getLog().warn("Project " + currentProject.getName()
							+ " uses artifact not in dependency management: " + artifact);
					allGood = false;
				}
			}
		}
		if (allGood) {
			getLog().info("Good for you. All projects use artifacts from the dependency management");
		}
	}

	/**
	 * Retrieves all artifacts specified in the dependencyManagement section of
	 * the project.
	 * 
	 * @return
	 */
	private Set<Artifact> getArtifactsInDependencyManagement() {
		Set<Artifact> artifactsInDependencyManagement = new HashSet<Artifact>();
		for (Dependency dependency : project.getDependencyManagement().getDependencies()) {
			artifactsInDependencyManagement.add(toArtifact(dependency));
		}
		return artifactsInDependencyManagement;
	}

	/**
	 * Converts the provided dependency to an artifact.
	 * 
	 * @param dependency
	 * @return
	 */
	private Artifact toArtifact(Dependency dependency) {
		return new DefaultArtifact(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(),
				dependency.getScope(), dependency.getType(), dependency.getClassifier(), artifactHandler);
	}

}
