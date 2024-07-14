package com.pinecone.framework.util.name;

public abstract class ArchName implements Name {
    protected String    mszName;

    protected ArchName( String szName ) {
        this.mszName = szName;
    }

    @Override
    public void  setName( String szName ){
        this.mszName = szName;
    }

    @Override
    public void  asStandardizedName( String szStandardizedName ) {
        this.mszName = szStandardizedName;
    }

    @Override
    public String toJSONString() {
        return "\"" + this.toString() + "\"";
    }

    @Override
    public boolean equals( Object obj ) {
        if( obj instanceof Name ) {
            if( obj instanceof Namespace ) {
                return this.getFullName().equals( ( (Namespace)obj ).getFullName() );
            }
            return this.getName().equals( ( (Name)obj ).getName() );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getFullName().hashCode();
    }
}
