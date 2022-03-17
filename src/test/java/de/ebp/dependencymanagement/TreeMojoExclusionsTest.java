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
            inOrder.verify(mockedLog).info("\\- com.google.dagger:dagger:jar:2.41:compile");
            // this SHOULD be missing
            // inOrder.verify(mockedLog).info("   \\- javax.inject:javax.inject:jar:1:compile");
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
            inOrder.verify(mockedLog).info("\\- com.google.dagger:dagger-android:jar:2.41:compile");
            inOrder.verify(mockedLog).info("   +- com.google.dagger:dagger:jar:2.41:compile");
            inOrder.verify(mockedLog).info("   |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("   +- com.google.dagger:dagger-lint-aar:jar:2.41:compile");
            inOrder.verify(mockedLog).info("   \\- androidx.annotation:annotation:jar:1.2.0:compile");
            // this SHOULD be missing
            // inOrder.verify(mockedLog).info("   \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
