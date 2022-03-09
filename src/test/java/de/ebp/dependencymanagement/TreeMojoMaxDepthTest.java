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
            inOrder.verify(mockedLog).info("+- tech.units:indriya:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("+- org.slf4j:slf4j-api:jar:1.7.36:compile");
            inOrder.verify(mockedLog).info("\\- junit:junit:jar:4.13.2:test");
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
            inOrder.verify(mockedLog).info("+- tech.units:indriya:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- javax.measure:unit-api:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- tech.uom.lib:uom-lib-common:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  +- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  +- org.apiguardian:apiguardian-api:jar:1.1.1:compile");
            inOrder.verify(mockedLog).info("|  +- org.osgi:org.osgi.annotation:jar:6.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- org.osgi:org.osgi.compendium:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- org.osgi:org.osgi.core:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- org.hamcrest:hamcrest-library:jar:2.2:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-api:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-params:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-engine:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.platform:junit-platform-launcher:jar:1.7.1:test");
            inOrder.verify(mockedLog).info("|  \\- org.junit.platform:junit-platform-runner:jar:1.7.1:test");
            inOrder.verify(mockedLog).info("+- org.slf4j:slf4j-api:jar:1.7.36:compile");
            inOrder.verify(mockedLog).info("|  \\- junit:junit:jar:4.13:test");
            inOrder.verify(mockedLog).info("\\- junit:junit:jar:4.13.2:test");
            inOrder.verify(mockedLog).info("   +- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("   \\- org.hamcrest:hamcrest-library:jar:1.3:test");
            inOrder.verifyNoMoreInteractions();
        });
    }

    @Test
    public void testTreeDepth3() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/depth3", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-depth3:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("+- tech.units:indriya:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- javax.measure:unit-api:jar:2.1.3:compile");
            inOrder.verify(mockedLog).info("|  +- tech.uom.lib:uom-lib-common:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  +- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  +- org.apiguardian:apiguardian-api:jar:1.1.1:compile");
            inOrder.verify(mockedLog).info("|  +- org.osgi:org.osgi.annotation:jar:6.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- org.osgi:org.osgi.compendium:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- org.osgi:org.osgi.core:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- org.hamcrest:hamcrest-library:jar:2.2:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-api:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-params:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.jupiter:junit-jupiter-engine:jar:5.7.1:test");
            inOrder.verify(mockedLog).info("|  +- org.junit.platform:junit-platform-launcher:jar:1.7.1:test");
            inOrder.verify(mockedLog).info("|  \\- org.junit.platform:junit-platform-runner:jar:1.7.1:test");
            inOrder.verify(mockedLog).info("+- org.slf4j:slf4j-api:jar:1.7.36:compile");
            inOrder.verify(mockedLog).info("|  \\- junit:junit:jar:4.13:test");
            inOrder.verify(mockedLog).info("\\- junit:junit:jar:4.13.2:test");
            inOrder.verify(mockedLog).info("   +- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("   \\- org.hamcrest:hamcrest-library:jar:1.3:test");
            inOrder.verify(mockedLog).info("      \\- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
