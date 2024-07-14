package com.pinecone.framework.util.name;

public abstract class ArchNamespaceNode extends ArchName implements Namespace {
    protected String           mszSeparator;

    protected ArchNamespaceNode( String szName, String separator ) {
        super( szName );
        this.mszSeparator = separator;
    }

    @Override
    public String getNodeName() {
        return this.mszName;
    }

    @Override
    public String getSeparator() {
        return this.mszSeparator;
    }

    @Override
    public void setSeparator( String separator ) {
        this.mszSeparator = separator;
    }

    @Override
    public String toString() {
        return this.getSimpleName();
    }
}
