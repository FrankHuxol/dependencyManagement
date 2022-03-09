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
                        "|  +- com.google.guava:failureaccess:jar:1.0.1:compile",
                        "|  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile",
                        "|  +- com.google.code.findbugs:jsr305:jar:3.0.2:compile",
                        "|  +- org.checkerframework:checker-qual:jar:3.12.0:compile",
                        "|  +- com.google.errorprone:error_prone_annotations:jar:2.11.0:compile",
                        "|  \\- com.google.j2objc:j2objc-annotations:jar:1.3:compile",
                        "+- org.slf4j:slf4j-api:jar:1.7.21:compile",
                        "|  \\- junit:junit:jar:4.12:test",
                        "\\- junit:junit:jar:4.13.1:test",
                        "   +- org.hamcrest:hamcrest-core:jar:1.3:compile",
                        "   \\- org.hamcrest:hamcrest-library:jar:1.3:test"));

        verifyNoMoreInteractions(mockedLog);
    }
}
