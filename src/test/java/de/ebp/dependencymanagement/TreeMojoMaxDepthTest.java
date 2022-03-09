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

    @Test
    public void testTreeDepth2() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/depth2", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockedLog, atLeastOnce()).info(captor.capture());

        assertThat(captor.getAllValues(),
                IsIterableContainingInOrder.contains(
                        "Dependency tree of dependencymanagement configuration:",
                        "de.ebp:tree-mojo-test-depth2:pom:0.0.1-SNAPSHOT",
                        "+- com.google.guava:guava:jar:31.1-jre:compile",
                        "|  +- com.google.code.findbugs:jsr305:3.0.2",
                        "|  +- com.google.errorprone:error_prone_annotations:2.11.0",
                        "|  +- com.google.guava:failureaccess:1.0.1",
                        "|  +- com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava",
                        "|  +- com.google.j2objc:j2objc-annotations:1.3",
                        "|  \\- org.checkerframework:checker-qual:3.12.0",
                        "+- org.slf4j:slf4j-api:jar:1.7.21:compile",
                        "\\- junit:junit:jar:4.13.1:test",
                        "|  +- org.hamcrest:hamcrest-core:1.3"));

        verifyNoMoreInteractions(mockedLog);
    }
}
