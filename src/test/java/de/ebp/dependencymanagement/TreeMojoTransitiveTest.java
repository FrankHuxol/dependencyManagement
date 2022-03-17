package de.ebp.dependencymanagement;

import org.junit.Test;

public class TreeMojoTransitiveTest extends BaseTreeMojoTest {

    @Test
    public void testDirectScopeTestDoesNotTakeTransitiveScopeTestIntoAccount() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/transitive/testScopeWithTransitiveTestScope", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Gathering dependency tree of dependencyManagement section. May take a while");
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-testScopeWithTransitiveTestScope:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("+- tech.units:indriya:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- javax.measure:unit-api:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- tech.uom.lib:uom-lib-common:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  +- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  +- org.apiguardian:apiguardian-api:jar:1.1.1:compile");
            inOrder.verify(mockedLog).info("|  +- org.hamcrest:hamcrest-library:jar:2.2:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-api:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-params:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-engine:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.platform:junit-platform-launcher:jar:1.7.1:test");
            inOrder.verify(mockedLog).info("|  \\- org.junit.platform:junit-platform-runner:jar:1.7.1:test");
            inOrder.verify(mockedLog).info("+- org.slf4j:slf4j-api:jar:1.7.36:compile");
            inOrder.verify(mockedLog).info("|  \\- junit:junit:jar:4.13:test");
            inOrder.verify(mockedLog).info("\\- junit:junit:jar:4.13.2:test");
            inOrder.verify(mockedLog).info("   \\- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
