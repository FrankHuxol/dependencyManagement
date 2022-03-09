package de.ebp.dependencymanagement;

import org.junit.Test;

public class TreeMojoMaxDepthTest extends BaseTreeMojoTest {

    @Test
    public void testTreeDepth1() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/depth1", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-depth1:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("+- com.google.guava:guava:jar:31.1-jre:compile");
            inOrder.verify(mockedLog).info("+- org.slf4j:slf4j-api:jar:1.7.21:compile");
            inOrder.verify(mockedLog).info("\\- junit:junit:jar:4.13.1:test");
            inOrder.verifyNoMoreInteractions();
        });
    }

    @Test
    public void testTreeDepth2() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/depth2", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-depth2:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("+- com.google.guava:guava:jar:31.1-jre:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.guava:failureaccess:jar:1.0.1:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.code.findbugs:jsr305:jar:3.0.2:compile");
            inOrder.verify(mockedLog).info("|  +- org.checkerframework:checker-qual:jar:3.12.0:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.errorprone:error_prone_annotations:jar:2.11.0:compile");
            inOrder.verify(mockedLog).info("|  \\- com.google.j2objc:j2objc-annotations:jar:1.3:compile");
            inOrder.verify(mockedLog).info("+- org.slf4j:slf4j-api:jar:1.7.21:compile");
            inOrder.verify(mockedLog).info("|  \\- junit:junit:jar:4.12:test");
            inOrder.verify(mockedLog).info("\\- junit:junit:jar:4.13.1:test");
            inOrder.verify(mockedLog).info("   +- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("   \\- org.hamcrest:hamcrest-library:jar:1.3:test");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
