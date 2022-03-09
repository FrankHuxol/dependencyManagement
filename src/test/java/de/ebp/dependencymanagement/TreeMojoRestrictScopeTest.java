package de.ebp.dependencymanagement;

import org.junit.Test;

public class TreeMojoRestrictScopeTest extends BaseTreeMojoTest {

    @Test
    public void testCompileScopeOnly() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/compileOnly", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-compileOnly:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("+- tech.units:indriya:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- javax.measure:unit-api:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- tech.uom.lib:uom-lib-common:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  +- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  \\- org.apiguardian:apiguardian-api:jar:1.1.1:compile");
            inOrder.verify(mockedLog).info("\\- org.slf4j:slf4j-api:jar:1.7.36:compile");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
