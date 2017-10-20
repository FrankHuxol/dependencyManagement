package dependencymanagementlist;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
		LoadingCache<Artifact, Set<MavenProject>> offenders = CacheBuilder.newBuilder()
				.build(new CacheLoader<Artifact, Set<MavenProject>>() {
					@Override
					public Set<MavenProject> load(Artifact key) throws Exception {
						return Sets.newHashSet();
					}
				});
		for (MavenProject currentProject : session.getAllProjects()) {
			Set<Artifact> usedArtifacts = getArtifactsOfProject(currentProject);
			SetView<Artifact> artifactsUsedButNotInDependencyManagement = Sets.difference(usedArtifacts,
					theAllowedArtifacts);
			if (!artifactsUsedButNotInDependencyManagement.isEmpty()) {
				for (Artifact artifact : Ordering.natural().nullsFirst()
						.immutableSortedCopy(artifactsUsedButNotInDependencyManagement)) {
					offenders.getUnchecked(artifact).add(currentProject);
				}
			}
		}
		ConcurrentMap<Artifact, Set<MavenProject>> offendersAsMap = offenders.asMap();
		if (offendersAsMap.isEmpty()) {
			getLog().info("Good for you. All projects use artifacts from the dependency management");
		} else {
			for (Entry<Artifact, Set<MavenProject>> entry : offendersAsMap.entrySet()) {
				getLog().warn("Artifact not in dependency management used: " + entry.getKey() + " (used by "
						+ entry.getValue() + ")");
			}
		}
	}

	/**
	 * Retrieves the artifacts used by the provided project.
	 * 
	 * @param theProject
	 * @return
	 */
	private Set<Artifact> getArtifactsOfProject(MavenProject theProject) {
		Set<Artifact> usedArtifacts = Sets.newHashSet();
		List<Dependency> dependencies = theProject.getDependencies();
		for (Dependency dependency : dependencies) {
			usedArtifacts.add(toArtifact(dependency));
		}
		return usedArtifacts;
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
