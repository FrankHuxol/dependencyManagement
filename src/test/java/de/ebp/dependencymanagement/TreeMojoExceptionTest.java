package de.ebp.dependencymanagement;

import org.junit.Test;

public class TreeMojoExceptionTest extends BaseTreeMojoTest {

    @Test
    public void testException() throws Exception {
        TreeMojo testedTreeMojo = (TreeMojo) prepareMojo("src/test/resources/unit/tree-mojo/exception", "tree");

        // run test
        testedTreeMojo.execute();

        // verify
        verify(inOrder -> {
            inOrder.verify(mockedLog).info("Gathering dependency tree of dependencyManagement section. May take a while");
            inOrder.verify(mockedLog).info("Dependency tree of dependencymanagement configuration:");
            inOrder.verify(mockedLog).info("de.ebp:tree-mojo-test-exception:pom:0.0.1-SNAPSHOT");
            inOrder.verify(mockedLog).info("\\- com.github.jnr:jnr-ffi:jar:1.0.0:compile");
            // The following two can't be resolved due to incorrect versions. They should still appear here
            inOrder.verify(mockedLog).info("   +- com.github.jnr:jffi:jar:[1.2.1, 1.3.0):compile");
            inOrder.verify(mockedLog).info("   +- com.github.jnr:jffi:jar:[1.2.1, 1.3.0):runtime");
            inOrder.verify(mockedLog).info("   +- org.ow2.asm:asm:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   +- org.ow2.asm:asm-commons:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   |  \\- org.ow2.asm:asm-tree:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   |     \\- org.ow2.asm:asm:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   +- org.ow2.asm:asm-analysis:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   |  \\- org.ow2.asm:asm-tree:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   |     \\- org.ow2.asm:asm:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   +- org.ow2.asm:asm-tree:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   |  \\- org.ow2.asm:asm:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   +- org.ow2.asm:asm-util:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   |  \\- org.ow2.asm:asm-tree:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   |     \\- org.ow2.asm:asm:jar:4.0:compile");
            inOrder.verify(mockedLog).info("   \\- com.github.jnr:jnr-x86asm:jar:[1.0.2,):compile");
            inOrder.verifyNoMoreInteractions();
        });
    }
}
