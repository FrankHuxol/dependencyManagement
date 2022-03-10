package de.ebp.dependencymanagement.dependency;

import com.google.common.collect.ComparisonChain;
import org.apache.maven.model.Dependency;

import java.util.Comparator;

/**
 * Comparator for dependencies. Takes group, artifact, type and version into account
 */
public class DependencyComparator implements Comparator<Dependency> {
    @Override
    public int compare(Dependency first, Dependency second) {

        return ComparisonChain
                .start()
                .compare(first.getGroupId(), second.getGroupId())
                .compare(first.getArtifactId(), second.getArtifactId())
                .compare(first.getType(), second.getType())
                .compare(first.getVersion(), second.getVersion())
                .result();
    }
}
