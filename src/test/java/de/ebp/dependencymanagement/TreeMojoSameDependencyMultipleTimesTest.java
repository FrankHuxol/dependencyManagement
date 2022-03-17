package de.ebp.dependencymanagement;

import org.junit.Test;

public class TreeMojoSameDependencyMultipleTimesTest extends BaseTreeMojoTest {

    @Test
    public void testSameVersion() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/sameDependencyMultipleTimes/sameVersion", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Gathering dependency tree of dependencyManagement section. May take a while");
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-sameDependencyMultipleTimes-sameVersion:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("+- si.uom:si-units:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  +- javax.measure:unit-api:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  +- tech.units:indriya:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  |  +- javax.measure:unit-api:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  |  |  \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("|  |  +- tech.uom.lib:uom-lib-common:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  |  |  +- javax.measure:unit-api:jar:2.0:compile");
            inOrder.verify(mockedLog).info("|  |  |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  |  +- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  |  +- org.osgi:org.osgi.annotation:jar:6.0.0:provided");
            inOrder.verify(mockedLog).info("|  |  +- org.osgi:org.osgi.compendium:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  |  \\- org.osgi:org.osgi.core:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- si.uom:si-quantity:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  |  \\- javax.measure:unit-api:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  |     \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("|  \\- jakarta.annotation:jakarta.annotation-api:jar:1.3.4:compile");
            inOrder.verify(mockedLog).info("\\- si.uom:si-quantity:jar:2.1:compile");
            inOrder.verify(mockedLog).info("   \\- javax.measure:unit-api:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("      \\- skipped already printed dependencies:::-:");
            inOrder.verifyNoMoreInteractions();
        });
    }

    @Test
    public void testDifferentVersion() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/sameDependencyMultipleTimes/differentVersion", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Gathering dependency tree of dependencyManagement section. May take a while");
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-sameDependencyMultipleTimes-differentVersion:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("+- si.uom:si-units:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  +- javax.measure:unit-api:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  +- tech.units:indriya:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  |  +- javax.measure:unit-api:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  |  |  \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("|  |  +- tech.uom.lib:uom-lib-common:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  |  |  +- javax.measure:unit-api:jar:2.0:compile");
            inOrder.verify(mockedLog).info("|  |  |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  |  +- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  |  +- org.osgi:org.osgi.annotation:jar:6.0.0:provided");
            inOrder.verify(mockedLog).info("|  |  +- org.osgi:org.osgi.compendium:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  |  \\- org.osgi:org.osgi.core:jar:5.0.0:provided");
            inOrder.verify(mockedLog).info("|  +- si.uom:si-quantity:jar:2.1:compile");
            inOrder.verify(mockedLog).info("|  |  \\- javax.measure:unit-api:jar:2.1.2:compile");
            inOrder.verify(mockedLog).info("|  |     \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("|  \\- jakarta.annotation:jakarta.annotation-api:jar:1.3.4:compile");
            inOrder.verify(mockedLog).info("\\- si.uom:si-quantity:jar:2.0.1:compile");
            inOrder.verify(mockedLog).info("   \\- javax.measure:unit-api:jar:2.0:compile");
            inOrder.verify(mockedLog).info("      \\- skipped already printed dependencies:::-:");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
