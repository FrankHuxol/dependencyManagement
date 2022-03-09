package de.ebp.dependencymanagement;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class TreeMojoMaxDepthTest extends BaseTreeMojoTest {

    @Test
    public void testTreeDepth1() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/depth1", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockedLog, atLeastOnce()).info(captor.capture());

        assertThat(captor.getAllValues(),
                IsIterableContainingInOrder.contains(
                        "Dependency tree of dependencymanagement configuration:",
                        "de.ebp:tree-mojo-test-depth1:pom:0.0.1-SNAPSHOT",
                        "+- com.google.guava:guava:jar:31.1-jre:compile",
                        "+- org.slf4j:slf4j-api:jar:1.7.21:compile",
                        "\\- junit:junit:jar:4.13.1:test"));

        verifyNoMoreInteractions(mockedLog);
    }
}
