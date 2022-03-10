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
            inOrder.verify(mockedLog).info("+- junit:junit:jar:4.13.2:test");
            inOrder.verify(mockedLog).info("|  +- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("|  \\- org.hamcrest:hamcrest-library:jar:1.3:test");
            inOrder.verify(mockedLog).info("|     \\- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("\\- com.google.code.gson:gson:jar:2.9.0:compile");
            inOrder.verify(mockedLog).info("   \\- junit:junit:jar:4.13.2:test");
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
            inOrder.verify(mockedLog).info("+- junit:junit:jar:4.13.2:test");
            inOrder.verify(mockedLog).info("|  +- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("|  \\- org.hamcrest:hamcrest-library:jar:1.3:test");
            inOrder.verify(mockedLog).info("|     \\- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("+- com.google.code.gson:gson:jar:2.9.0:compile");
            inOrder.verify(mockedLog).info("|  \\- junit:junit:jar:4.13.2:test");
            inOrder.verify(mockedLog).info("|     \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("\\- io.gsonfire:gson-fire:jar:1.8.5:compile");
            inOrder.verify(mockedLog).info("   +- com.google.code.gson:gson:jar:2.8.6:compile");
            inOrder.verify(mockedLog).info("   |  \\- junit:junit:jar:4.12:test");
            inOrder.verify(mockedLog).info("   |     \\- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("   \\- junit:junit:jar:4.13.1:test");
            inOrder.verify(mockedLog).info("      +- org.hamcrest:hamcrest-core:jar:1.3:compile");
            inOrder.verify(mockedLog).info("      \\- org.hamcrest:hamcrest-library:jar:1.3:test");
            inOrder.verify(mockedLog).info("         \\- skipped already printed dependencies:::-:");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
