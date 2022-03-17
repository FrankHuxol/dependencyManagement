package de.ebp.dependencymanagement.tree;

import java.util.List;

public class ResolutionOptions {

    private int maxDepth;
    private List<String> scopes;
    private boolean skipDuplicates;

    public ResolutionOptions(int theMaxDepth, List<String> theScopes, boolean theSkipDuplicates) {
        this.maxDepth = theMaxDepth;
        this.scopes = theScopes;
        this.skipDuplicates = theSkipDuplicates;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public boolean skipDuplicates() {
        return skipDuplicates;
    }

    public ResolutionOptions withReducedMaxDepth() {
        return new ResolutionOptions(maxDepth - 1, scopes, skipDuplicates);
    }
}
