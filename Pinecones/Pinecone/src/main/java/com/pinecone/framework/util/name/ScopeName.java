package com.pinecone.framework.util.name;

public class ScopeName extends ArchName implements Name {
    protected String    mszDomain;

    public ScopeName( String szName, String szDomain ) {
        super( szName );
        this.mszDomain = szDomain;
    }

    public ScopeName( String szName ) {
        this( szName, "" );
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public String getFullName(){
        return this.getDomain() + this.getName();
    }

    @Override
    public String getDomain(){
        return this.mszDomain;
    }

    @Override
    public String toString() {
        return this.getFullName();
    }
}
