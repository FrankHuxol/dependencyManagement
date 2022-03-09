package de.ebp.dependencymanagement;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class TreeMojoMaxDepthTest extends BaseTreeMojoTest {

    TreeMojo testedTreeMojo;

    @Test
    public void testTreeDepth1() throws Exception {
        MavenProject project = mojoRule.readMavenProject(new File("src/test/resources/unit/tree-mojo/depth1"));

        // Generate session
        MavenSession session = mojoRule.newMavenSession(project);

        // add localRepo - framework doesn't do this on its own
        ArtifactRepository localRepo = createLocalArtifactRepository();
        session.getRequest().setLocalRepository(localRepo);

        // Generate Execution and Mojo for testing
        MojoExecution execution = mojoRule.newMojoExecution("tree");
        testedTreeMojo = (TreeMojo) mojoRule.lookupConfiguredMojo(session, execution);
        assertThat(testedTreeMojo, notNullValue());

        testedTreeMojo.setLog(mockedLog);

        mojoRule.executeMojo(session, project, execution);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockedLog, atLeastOnce()).info(captor.capture());

        assertThat(captor.getAllValues(),
                IsIterableContainingInOrder.contains(
                        "Dependency tree of dependencymanagement configuration:",
                        "de.ebp:tree-mojo-test:pom:0.0.1-SNAPSHOT",
                        "+- com.google.guava:guava:jar:31.1-jre:compile",
                        "+- org.slf4j:slf4j-api:jar:1.7.21:compile",
                        "\\- junit:junit:jar:4.13.1:test"));

        verifyNoMoreInteractions(mockedLog);
    }
}
