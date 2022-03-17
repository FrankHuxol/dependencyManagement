package de.ebp.dependencymanagement;

import org.junit.Test;

public class TreeMojoExclusionsTest extends BaseTreeMojoTest {

    @Test
    public void testDirectExclusion() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/exclusions/direct", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Gathering dependency tree of dependencyManagement section. May take a while");
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-exclusions-direct:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("\\- com.google.guava:guava:jar:31.1-jre:compile");
            // this SHOULD be missing
            // inOrder.verify(mockedLog).info("   +- com.google.guava:failureaccess:jar:1.0.1:compile");
            inOrder.verify(mockedLog).info("   +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile");
            inOrder.verify(mockedLog).info("   +- com.google.code.findbugs:jsr305:jar:3.0.2:compile");
            inOrder.verify(mockedLog).info("   +- org.checkerframework:checker-qual:jar:3.12.0:compile");
            inOrder.verify(mockedLog).info("   +- com.google.errorprone:error_prone_annotations:jar:2.11.0:compile");
            inOrder.verify(mockedLog).info("   \\- com.google.j2objc:j2objc-annotations:jar:1.3:compile");
            inOrder.verifyNoMoreInteractions();
        });
    }

    @Test
    public void testIndirectExclusion() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/exclusions/indirect", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Gathering dependency tree of dependencyManagement section. May take a while");
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-exclusions-indirect:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("\\- com.google.inject:guice:jar:5.1.0:compile");
            inOrder.verify(mockedLog).info("   +- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("   +- aopalliance:aopalliance:jar:1.0:compile");
            inOrder.verify(mockedLog).info("   +- com.google.guava:guava:jar:30.1-jre:compile");
            // this SHOULD be missing
            // inOrder.verify(mockedLog).info("   |  +- com.google.guava:failureaccess:jar:1.0.1:compile");
            inOrder.verify(mockedLog).info("   |  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile");
            inOrder.verify(mockedLog).info("   |  +- com.google.code.findbugs:jsr305:jar:3.0.2:compile");
            inOrder.verify(mockedLog).info("   |  +- org.checkerframework:checker-qual:jar:3.5.0:compile");
            inOrder.verify(mockedLog).info("   |  +- com.google.errorprone:error_prone_annotations:jar:2.3.4:compile");
            inOrder.verify(mockedLog).info("   |  \\- com.google.j2objc:j2objc-annotations:jar:1.3:compile");
            inOrder.verify(mockedLog).info("   \\- org.ow2.asm:asm:jar:9.2:compile");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
