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
            inOrder.verify(mockedLog).info("+- com.google.dagger:dagger-android-support:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.dagger:dagger:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.dagger:dagger-android:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  +- com.google.dagger:dagger:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  |  \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("|  |  +- com.google.dagger:dagger-lint-aar:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  +- androidx.annotation:annotation:jar:1.2.0:compile");
            inOrder.verify(mockedLog).info("|  |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.dagger:dagger-lint-aar:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.activity:activity:jar:1.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.annotation:annotation:jar:1.2.0:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.appcompat:appcompat:jar:1.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.fragment:fragment:jar:1.3.6:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.lifecycle:lifecycle-common:jar:2.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.lifecycle:lifecycle-viewmodel:jar:2.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.lifecycle:lifecycle-viewmodel-savedstate:jar:2.3.1:compile");
            inOrder.verify(mockedLog).info("|  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("\\- com.google.dagger:dagger-android:jar:2.41:compile");
            inOrder.verify(mockedLog).info("   +- com.google.dagger:dagger:jar:2.41:compile");
            inOrder.verify(mockedLog).info("   |  \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("   +- com.google.dagger:dagger-lint-aar:jar:2.41:compile");
            inOrder.verify(mockedLog).info("   +- androidx.annotation:annotation:jar:1.2.0:compile");
            inOrder.verify(mockedLog).info("   \\- javax.inject:javax.inject:jar:1:compile");
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
            inOrder.verify(mockedLog).info("+- com.google.dagger:dagger-android-support:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.dagger:dagger:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.dagger:dagger-android:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  +- com.google.dagger:dagger:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  |  \\- skipped already printed dependencies:::-:");
            inOrder.verify(mockedLog).info("|  |  +- com.google.dagger:dagger-lint-aar:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  |  +- androidx.annotation:annotation:jar:1.2.0:compile");
            inOrder.verify(mockedLog).info("|  |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("|  +- com.google.dagger:dagger-lint-aar:jar:2.41:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.activity:activity:jar:1.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.annotation:annotation:jar:1.2.0:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.appcompat:appcompat:jar:1.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.fragment:fragment:jar:1.3.6:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.lifecycle:lifecycle-common:jar:2.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.lifecycle:lifecycle-viewmodel:jar:2.3.1:compile");
            inOrder.verify(mockedLog).info("|  +- androidx.lifecycle:lifecycle-viewmodel-savedstate:jar:2.3.1:compile");
            inOrder.verify(mockedLog).info("|  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("\\- com.google.dagger:dagger-android:jar:2.40:compile");
            inOrder.verify(mockedLog).info("   +- com.google.dagger:dagger:jar:2.40:compile");
            inOrder.verify(mockedLog).info("   |  \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verify(mockedLog).info("   +- com.google.dagger:dagger-lint-aar:jar:2.40:compile");
            inOrder.verify(mockedLog).info("   +- androidx.annotation:annotation:jar:1.2.0:compile");
            inOrder.verify(mockedLog).info("   \\- javax.inject:javax.inject:jar:1:compile");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
