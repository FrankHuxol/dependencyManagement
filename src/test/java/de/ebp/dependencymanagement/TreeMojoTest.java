package de.ebp.dependencymanagement;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.DefaultMaven;
import org.apache.maven.Maven;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequestPopulator;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManagerFactory;
import org.eclipse.aether.repository.LocalRepository;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TreeMojoTest {

	TreeMojo testedTreeMojo;
	@Mock
	private Log mockedLog;

	@Rule
	public MojoRule rule = new MojoRule() {

		protected MavenSession newMavenSession() {
			try {
				MavenExecutionRequest request = new DefaultMavenExecutionRequest();
				MavenExecutionResult result = new DefaultMavenExecutionResult();

				// populate sensible defaults, including repository basedir and
				// remote repos
				MavenExecutionRequestPopulator populator;
				populator = getContainer().lookup(MavenExecutionRequestPopulator.class);
				populator.populateDefaults(request);

				// this is needed to allow java profiles to get resolved; i.e.
				// avoid during project builds:
				// [ERROR] Failed to determine Java version for profile
				// java-1.5-detected @ org.apache.commons:commons-parent:22,
				// /Users/alex/.m2/repository/org/apache/commons/commons-parent/22/commons-parent-22.pom,
				// line 909, column 14
				request.setSystemProperties(System.getProperties());

				// and this is needed so that the repo session in the maven
				// session
				// has a repo manager, and it points at the local repo
				// (cf MavenRepositorySystemUtils.newSession() which is what is
				// otherwise done)
				DefaultMaven maven = (DefaultMaven) getContainer().lookup(Maven.class);
				DefaultRepositorySystemSession repoSession = (DefaultRepositorySystemSession) maven
						.newRepositorySession(request);
				repoSession.setLocalRepositoryManager(new SimpleLocalRepositoryManagerFactory().newInstance(repoSession,
						new LocalRepository(request.getLocalRepository().getBasedir())));

				@SuppressWarnings("deprecation")
				MavenSession session = new MavenSession(getContainer(), repoSession, request, result);
				return session;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Extends the super to use the new {@link #newMavenSession()}
		 * introduced here which sets the defaults one expects from maven; the
		 * standard test case leaves a lot of things blank
		 */
		public MavenSession newMavenSession(MavenProject project) {
			MavenSession session = newMavenSession();
			session.setCurrentProject(project);
			session.setProjects(Arrays.asList(project));
			return session;
		}

		@Override
		protected void before() throws Throwable {
			MockitoAnnotations.initMocks(TreeMojoTest.this);

			ProjectBuildingRequest buildingRequest = newMavenSession().getProjectBuildingRequest();
			ProjectBuilder projectBuilder = lookup(ProjectBuilder.class);
			MavenProject project = projectBuilder
					.build(new File("src/test/resources/unit/tree-mojo/pom.xml"), buildingRequest).getProject();

			testedTreeMojo = (TreeMojo) lookupConfiguredMojo(project, "tree");

			assertThat(testedTreeMojo, notNullValue());

			testedTreeMojo.setLog(mockedLog);
		}

		@Override
		protected void after() {
		}
	};

	@Test
	public void testSomething() throws Exception {
		testedTreeMojo.execute();

		InOrder inOrder = inOrder(mockedLog);
		inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test:pom:0.0.1-SNAPSHOT");
	}

}
