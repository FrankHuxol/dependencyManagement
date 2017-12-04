package de.ebp.dependencymanagement;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "tree", requiresDependencyResolution = ResolutionScope.TEST)
public class TreeMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().debug("Hello World");
	}

}
