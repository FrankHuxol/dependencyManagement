package de.ebp.dependencymanagement;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TreeMojoTest {

	TreeMojo testedTreeMojo;
	@Mock
	private Log mockedLog;

	@Rule
	public MojoRule rule = new MojoRule() {

		@Override
		protected void before() throws Throwable {
			MockitoAnnotations.initMocks(TreeMojoTest.this);

			testedTreeMojo = (TreeMojo) rule.lookupMojo("tree", "src/test/resources/unit/tree-mojo/pom.xml");
			assertThat(testedTreeMojo, notNullValue());

			testedTreeMojo.setLog(mockedLog);
		}

		@Override
		protected void after() {
		}
	};

	@Test
	public void testSomething() throws Exception {
		testedTreeMojo.execute();

		verify(mockedLog).debug("Hello World");
	}

}
