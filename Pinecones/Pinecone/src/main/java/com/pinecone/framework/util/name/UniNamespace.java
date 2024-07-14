package com.pinecone.framework.util.name;

public class UniNamespace extends ArchNamespaceNode implements Namespace {
    protected Namespace        mParent;

    public UniNamespace( String name ) {
        this( name, null, Namespace.DEFAULT_SEPARATOR );
    }

    public UniNamespace( String name, Namespace parent ) {
        this( name, parent, Namespace.DEFAULT_SEPARATOR );
    }

    public UniNamespace( String name, Namespace parent, String separator ) {
        super( name, separator );
        this.mParent        = parent;
    }

    public UniNamespace( String name, String separator ) {
        this( name, null, separator );
    }

    @Override
    public Namespace parent() {
        return mParent;
    }

    @Override
    public void setParent( Namespace parent ) {
        this.mParent = parent;
    }

    @Override
    public String getSimpleName() {
        return this.getNodeName();
    }

    @Override
    public String getFullName() {
        if ( this.mParent == null ) {
            return this.mszName;
        }
        return this.mParent.getFullName() + this.mszSeparator + this.mszName;
    }

    @Override
    public  boolean equals( Object that ) {
        if( that instanceof Namespace ) {
            return this.getFullName().equals( ((Namespace) that).getFullName() );
        }
        return false;
    }
}
