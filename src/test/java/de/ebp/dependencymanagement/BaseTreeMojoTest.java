package de.ebp.dependencymanagement;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;

import java.io.File;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

public abstract class BaseTreeMojoTest {

    @Mock
    protected Log mockedLog;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public MojoRule mojoRule = new MojoRule() {

        @Override
        protected void before() {
            MockitoAnnotations.openMocks(BaseTreeMojoTest.this);
        }

        @Override
        protected void after() {
        }
    };

    /**
     * Generate a local repository
     *
     * @return local repository object
     */
    protected ArtifactRepository createLocalArtifactRepository() {
        return new MavenArtifactRepository("local",
                tempFolder.getRoot().toURI().toString(),
                new DefaultRepositoryLayout(),
                new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE),
                new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE)

        );
    }

    /**
     * Reads a mojo for the prepared project
     *
     * @param projectFolder The project folder to read
     * @param aGoal         The goal to read
     * @return The mojo for the provided goal
     * @throws Exception in case something goes wrong
     */
    protected AbstractMojo prepareMojo(String projectFolder, String aGoal) throws Exception {
        MavenProject project = mojoRule.readMavenProject(new File(projectFolder));

        // Generate session
        MavenSession session = mojoRule.newMavenSession(project);

        // add localRepo - framework doesn't do this on its own
        ArtifactRepository localRepo = createLocalArtifactRepository();
        session.getRequest().setLocalRepository(localRepo);

        session.getRequest().setRemoteRepositories(project.getRemoteArtifactRepositories());

        // Generate Execution and Mojo for testing
        MojoExecution execution = mojoRule.newMojoExecution(aGoal);
        AbstractMojo mojo = (AbstractMojo) mojoRule.lookupConfiguredMojo(session, execution);
        assertThat(mojo, notNullValue());
        mojo.setLog(mockedLog);
        return mojo;
    }

    /**
     * Convenience method to verify calls towards log in a particular order. If a verification fails, additional text output is created.
     *
     * @param inOrder A callback providing the actual verifications
     * @throws VerificationInOrderFailure In case the verification fails
     */
    protected void verify(Consumer<InOrder> inOrder) throws VerificationInOrderFailure {
        InOrder i = inOrder(mockedLog);
        try {
            inOrder.accept(i);
        } catch (VerificationInOrderFailure e) {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            i.verify(mockedLog, atLeastOnce()).info(captor.capture());
            assertThat("Tree verification failed due to: " + e.getMessage() + "\nFound these remaining logs", captor.getAllValues(), IsEmptyCollection.empty());
            throw e;
        }
    }


}
